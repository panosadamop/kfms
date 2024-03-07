import './home.scss';

import React, { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { Translate } from 'react-jhipster';
import { Row, Col, Alert } from 'reactstrap';

import { getLoginUrl, REDIRECT_URL } from 'app/shared/util/url-utils';
import { useAppSelector } from 'app/config/store';
import { FormOutput } from 'app/dynamicControls/FormOutput';

export const Home = () => {
  const account = useAppSelector(state => state.authentication.account);
  useEffect(() => {
    const redirectURL = localStorage.getItem(REDIRECT_URL);
    if (redirectURL) {
      localStorage.removeItem(REDIRECT_URL);
      location.href = `${location.origin}${redirectURL}`;
    }
  });

  return (
    <Row>
      <Col md="3" className="pad">
        <FormOutput />
      </Col>
      <Col md="9">
        <h2>
          <Translate contentKey="home.title">Welcome, Java Hipster!</Translate>
        </h2>
        <p className="lead">
          <Translate contentKey="home.subtitle">This is your homepage</Translate>
        </p>
        {account?.login ? (
          <div>
            <Alert color="success">
              <Translate contentKey="home.logged.message" interpolate={{ username: account.login }}>
                You are logged in as user {account.login}.
              </Translate>
            </Alert>
          </div>
        ) : (
          <div>
            <Alert color="warning">
              <Translate contentKey="global.messages.info.authenticated.prefix">If you want to </Translate>

              <a href={getLoginUrl()} className="alert-link">
                <Translate contentKey="global.messages.info.authenticated.link">sign in</Translate>
              </a>
              <Translate contentKey="global.messages.info.authenticated.suffix">
                , you can try the default accounts:
                <br />- Administrator (login=&quot;admin&quot; and password=&quot;admin&quot;)
                <br />- User (login=&quot;user&quot; and password=&quot;user&quot;).
              </Translate>
            </Alert>
          </div>
        )}
        <p>
          <Translate contentKey="home.question">If you have any question on JHipster:</Translate>
        </p>
      </Col>
    </Row>
  );
};

export default Home;
