import { IUserProfile } from 'app/shared/model/user-profile.model';

export interface ISkill {
  id?: number;
  name?: string;
  categoryName?: string;
  owners?: IUserProfile[];
}

export const defaultValue: Readonly<ISkill> = {};
