import { IProfileSkillValue } from 'app/shared/model/profile-skill-value.model';
import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ISkillShape {
  id?: number;
  title?: string;
  category?: string;
  skills?: IProfileSkillValue[];
  owners?: IUserProfile[];
}

export const defaultValue: Readonly<ISkillShape> = {};
