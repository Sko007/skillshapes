import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SkillShape from './skill-shape';
import SkillShapeDetail from './skill-shape-detail';
import SkillShapeUpdate from './skill-shape-update';
import SkillShapeDeleteDialog from './skill-shape-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SkillShapeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SkillShapeUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SkillShapeDetail} />
      <ErrorBoundaryRoute path={match.url} component={SkillShape} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={SkillShapeDeleteDialog} />
  </>
);

export default Routes;
