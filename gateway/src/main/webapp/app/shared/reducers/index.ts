import { combineReducers } from 'redux';
import { loadingBarReducer as loadingBar } from 'react-redux-loading-bar';

import locale, { LocaleState } from './locale';
import authentication, { AuthenticationState } from './authentication';
import applicationProfile, { ApplicationProfileState } from './application-profile';

import administration, { AdministrationState } from 'app/modules/administration/administration.reducer';
import userManagement, { UserManagementState } from './user-management';
// prettier-ignore
import userProfile, {
  UserProfileState
} from 'app/entities/user-profile/user-profile.reducer';
// prettier-ignore
import skillShape, {
  SkillShapeState
} from 'app/entities/skill-shape/skill-shape.reducer';
// prettier-ignore
import profileSkillValue, {
  ProfileSkillValueState
} from 'app/entities/profile-skill-value/profile-skill-value.reducer';
// prettier-ignore
import skill, {
  SkillState
} from 'app/entities/skill/skill.reducer';
import skillshapes, { SkillshapesState } from 'app/skillshape/skillshape.reducer';
/* jhipster-needle-add-reducer-import - JHipster will add reducer here */

export interface IRootState {
  readonly authentication: AuthenticationState;
  readonly locale: LocaleState;
  readonly applicationProfile: ApplicationProfileState;
  readonly administration: AdministrationState;
  readonly userManagement: UserManagementState;
  readonly userProfile: UserProfileState;
  readonly skillShape: SkillShapeState;
  readonly profileSkillValue: ProfileSkillValueState;
  readonly skill: SkillState;
  readonly skillshapes: SkillshapesState;
  /* jhipster-needle-add-reducer-type - JHipster will add reducer type here */
  readonly loadingBar: any;
}

const rootReducer = combineReducers<IRootState>({
  authentication,
  locale,
  applicationProfile,
  administration,
  userManagement,
  userProfile,
  skillShape,
  profileSkillValue,
  skill,
  skillshapes,
  /* jhipster-needle-add-reducer-combine - JHipster will add reducer here */
  loadingBar,
});

export default rootReducer;
