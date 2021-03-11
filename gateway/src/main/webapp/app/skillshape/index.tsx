import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';
import Skillshapes from './skillshape.root';
import PrivateRoute from 'app/shared/auth/private-route';

import { AUTHORITIES } from 'app/config/constants';
const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/`} component={Skillshapes} />
    </Switch>
  </>
);

export default Routes;
