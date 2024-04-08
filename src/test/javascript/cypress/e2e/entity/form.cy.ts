import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Form e2e test', () => {
  const formPageUrl = '/form';
  const formPageUrlPattern = new RegExp('/form(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formSample = { formName: 'appearance', version: 'calm overconfidently regarding', createDate: '2024-04-08' };

  let form;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/forms+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/forms').as('postEntityRequest');
    cy.intercept('DELETE', '/api/forms/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (form) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/forms/${form.id}`,
      }).then(() => {
        form = undefined;
      });
    }
  });

  it('Forms menu should load Forms page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('form');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Form').should('exist');
    cy.url().should('match', formPageUrlPattern);
  });

  describe('Form page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Form page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/form/new$'));
        cy.getEntityCreateUpdateHeading('Form');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/forms',
          body: formSample,
        }).then(({ body }) => {
          form = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/forms+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/forms?page=0&size=20>; rel="last",<http://localhost/api/forms?page=0&size=20>; rel="first"',
              },
              body: [form],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Form page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('form');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formPageUrlPattern);
      });

      it('edit button click should load edit Form page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Form');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formPageUrlPattern);
      });

      it('edit button click should load edit Form page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Form');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formPageUrlPattern);
      });

      it('last delete button click should delete instance of Form', () => {
        cy.intercept('GET', '/api/forms/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('form').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formPageUrlPattern);

        form = undefined;
      });
    });
  });

  describe('new Form page', () => {
    beforeEach(() => {
      cy.visit(`${formPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Form');
    });

    it('should create an instance of Form', () => {
      cy.get(`[data-cy="formName"]`).type('regarding');
      cy.get(`[data-cy="formName"]`).should('have.value', 'regarding');

      cy.get(`[data-cy="formDescription"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="formDescription"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="formData"]`).type('../fake-data/blob/hipster.txt');
      cy.get(`[data-cy="formData"]`).invoke('val').should('match', new RegExp('../fake-data/blob/hipster.txt'));

      cy.get(`[data-cy="version"]`).type('audience meh');
      cy.get(`[data-cy="version"]`).should('have.value', 'audience meh');

      cy.get(`[data-cy="isDeleted"]`).should('not.be.checked');
      cy.get(`[data-cy="isDeleted"]`).click();
      cy.get(`[data-cy="isDeleted"]`).should('be.checked');

      cy.get(`[data-cy="isLocked"]`).should('not.be.checked');
      cy.get(`[data-cy="isLocked"]`).click();
      cy.get(`[data-cy="isLocked"]`).should('be.checked');

      cy.get(`[data-cy="createDate"]`).type('2024-04-07');
      cy.get(`[data-cy="createDate"]`).blur();
      cy.get(`[data-cy="createDate"]`).should('have.value', '2024-04-07');

      cy.get(`[data-cy="updateDate"]`).type('2024-04-08');
      cy.get(`[data-cy="updateDate"]`).blur();
      cy.get(`[data-cy="updateDate"]`).should('have.value', '2024-04-08');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        form = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formPageUrlPattern);
    });
  });
});
