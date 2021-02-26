import { ISkill } from 'app/shared/model/skill.model';
import { ISkillShape } from 'app/shared/model/skill-shape.model';

export interface IUserProfile {
  id?: number;
  firstName?: string;
  lastName?: string;
  email?: string;
  generalKnowledge?: string;
  skills?: ISkill[];
  skillshapes?: ISkillShape[];
}

export const defaultValue: Readonly<IUserProfile> = {};
