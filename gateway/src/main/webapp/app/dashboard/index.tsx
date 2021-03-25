import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import SkillShapeDashboard from './skill-shape-dashboard';
import SkillShapeDashboardDetail from './skill-shape-dashboard-detail';
import SkillShapeDashboardUpdate from './skill-shape-dashboard-update';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={SkillShapeDashboardUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={SkillShapeDashboardUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={SkillShapeDashboardDetail} />
      <ErrorBoundaryRoute path={match.url} component={SkillShapeDashboard} />
    </Switch>
  </>
);

export default Routes;
