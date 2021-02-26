import { ISkill } from 'app/shared/model/skill.model';
import { ISkillShape } from 'app/shared/model/skill-shape.model';

export interface IProfileSkillValue {
  id?: number;
  value?: number;
  skill?: ISkill;
  skillshapes?: ISkillShape[];
}

export const defaultValue: Readonly<IProfileSkillValue> = {};
