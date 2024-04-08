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

describe('FormTypes e2e test', () => {
  const formTypesPageUrl = '/form-types';
  const formTypesPageUrlPattern = new RegExp('/form-types(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const formTypesSample = { type: 'cria tensely' };

  let formTypes;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/form-types+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/form-types').as('postEntityRequest');
    cy.intercept('DELETE', '/api/form-types/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (formTypes) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/form-types/${formTypes.id}`,
      }).then(() => {
        formTypes = undefined;
      });
    }
  });

  it('FormTypes menu should load FormTypes page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('form-types');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('FormTypes').should('exist');
    cy.url().should('match', formTypesPageUrlPattern);
  });

  describe('FormTypes page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(formTypesPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create FormTypes page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/form-types/new$'));
        cy.getEntityCreateUpdateHeading('FormTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formTypesPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/form-types',
          body: formTypesSample,
        }).then(({ body }) => {
          formTypes = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/form-types+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              headers: {
                link: '<http://localhost/api/form-types?page=0&size=20>; rel="last",<http://localhost/api/form-types?page=0&size=20>; rel="first"',
              },
              body: [formTypes],
            },
          ).as('entitiesRequestInternal');
        });

        cy.visit(formTypesPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details FormTypes page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('formTypes');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formTypesPageUrlPattern);
      });

      it('edit button click should load edit FormTypes page and go back', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormTypes');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formTypesPageUrlPattern);
      });

      it('edit button click should load edit FormTypes page and save', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('FormTypes');
        cy.get(entityCreateSaveButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formTypesPageUrlPattern);
      });

      it('last delete button click should delete instance of FormTypes', () => {
        cy.intercept('GET', '/api/form-types/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('formTypes').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response.statusCode).to.equal(200);
        });
        cy.url().should('match', formTypesPageUrlPattern);

        formTypes = undefined;
      });
    });
  });

  describe('new FormTypes page', () => {
    beforeEach(() => {
      cy.visit(`${formTypesPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('FormTypes');
    });

    it('should create an instance of FormTypes', () => {
      cy.get(`[data-cy="type"]`).type('harmful highly angel');
      cy.get(`[data-cy="type"]`).should('have.value', 'harmful highly angel');

      cy.get(`[data-cy="description"]`).type('incidentally regarding supposing');
      cy.get(`[data-cy="description"]`).should('have.value', 'incidentally regarding supposing');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(201);
        formTypes = response.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response.statusCode).to.equal(200);
      });
      cy.url().should('match', formTypesPageUrlPattern);
    });
  });
});
