import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import ProfileSkillValue from './profile-skill-value';
import ProfileSkillValueDetail from './profile-skill-value-detail';
import ProfileSkillValueUpdate from './profile-skill-value-update';
import ProfileSkillValueDeleteDialog from './profile-skill-value-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ProfileSkillValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ProfileSkillValueUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ProfileSkillValueDetail} />
      <ErrorBoundaryRoute path={match.url} component={ProfileSkillValue} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ProfileSkillValueDeleteDialog} />
  </>
);

export default Routes;
