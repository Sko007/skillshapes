import React from 'react';
import { Switch } from 'react-router-dom';

// eslint-disable-next-line @typescript-eslint/no-unused-vars
import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import UserProfile from './user-profile';
import SkillShape from './skill-shape';
import ProfileSkillValue from './profile-skill-value';
import Skill from './skill';
/* jhipster-needle-add-route-import - JHipster will add routes here */

const Routes = ({ match }) => (
  <div>
    <Switch>
      {/* prettier-ignore */}
      <ErrorBoundaryRoute path={`${match.url}user-profile`} component={UserProfile} />
      <ErrorBoundaryRoute path={`${match.url}skill-shape`} component={SkillShape} />
      <ErrorBoundaryRoute path={`${match.url}profile-skill-value`} component={ProfileSkillValue} />
      <ErrorBoundaryRoute path={`${match.url}skill`} component={Skill} />
      {/* jhipster-needle-add-route-path - JHipster will add routes here */}
    </Switch>
  </div>
);

export default Routes;
