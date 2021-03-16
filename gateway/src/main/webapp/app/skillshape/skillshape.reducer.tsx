/* eslint no-console: off */
import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISkillshapes } from 'app/shared/model/skillshape.model';

export const ACTION_TYPES = {
  FETCH_SKILLSHAPES: 'skillshapes/FETCH_SKILLSHAPES',
  FETCH_TECHNOLOGIES: 'skillshapes/FETCH_TECHNOLOGIES',
  RESET: 'skillshapes/RESET',
};

const initialState = {
  loading: true,
  errorMessage: null,
  technologies: undefined,
  entity: undefined,
  category: undefined,
  title: undefined,
  level2: undefined,
  updating: false,
  updateSuccess: false,
};

interface Item {
  title: string;
  skills: Skills[];
  id: number;
  category: string;
}

interface Skills {
  id: number;
  name: string;
  skillId: number;
  value: number;
}

export type SkillshapesState = Readonly<typeof initialState>;

// Reducer
export default (state: SkillshapesState = initialState, action): SkillshapesState => {
  // console.log(`check reducer ${JSON.stringify(action.payload)}`);
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SKILLSHAPES):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };

    case FAILURE(ACTION_TYPES.FETCH_SKILLSHAPES):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SKILLSHAPES):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_TECHNOLOGIES):
      return {
        ...state,
        loading: false,
        technologies: action.payload.data,
        category: action.payload.data.map((item: Item) => {
          return { id: item.id, name: item.category };
        }),
        title: action.payload.data.map((item: Item) => {
          return { id: item.id, name: item.title, value: 5 };
        }),
        level2: action.payload.data.map((item: Item) => {
          return item.skills;
        }),
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = 'api/skillshapes';

// Actions
export const getUserData: ICrudPutAction<ISkillshapes> = () => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.FETCH_SKILLSHAPES,
    payload: axios.get<ISkillshapes>('/services/microservice/'),
  });
  // eslint-disable-next-line no-console
  //console.log(`check the result ${JSON.stringify(result)}`);
  return result;
};

export const getTechnologies: ICrudPutAction<ISkillshapes> = () => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.FETCH_TECHNOLOGIES,
    payload: axios.get<ISkillshapes>('/services/microservice/'),
  });
  // eslint-disable-next-line no-console
  // console.log(`check the result for Technologie ${JSON.stringify(result)}`);

  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
