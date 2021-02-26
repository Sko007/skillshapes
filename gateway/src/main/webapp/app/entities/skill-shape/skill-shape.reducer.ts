import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { ISkillShape, defaultValue } from 'app/shared/model/skill-shape.model';
import { apiPrefix } from 'app/shared/util/url-utils';

export const ACTION_TYPES = {
  FETCH_SKILLSHAPE_LIST: 'skillShape/FETCH_SKILLSHAPE_LIST',
  FETCH_SKILLSHAPE: 'skillShape/FETCH_SKILLSHAPE',
  CREATE_SKILLSHAPE: 'skillShape/CREATE_SKILLSHAPE',
  UPDATE_SKILLSHAPE: 'skillShape/UPDATE_SKILLSHAPE',
  DELETE_SKILLSHAPE: 'skillShape/DELETE_SKILLSHAPE',
  RESET: 'skillShape/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<ISkillShape>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type SkillShapeState = Readonly<typeof initialState>;

// Reducer

export default (state: SkillShapeState = initialState, action): SkillShapeState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_SKILLSHAPE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_SKILLSHAPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_SKILLSHAPE):
    case REQUEST(ACTION_TYPES.UPDATE_SKILLSHAPE):
    case REQUEST(ACTION_TYPES.DELETE_SKILLSHAPE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_SKILLSHAPE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_SKILLSHAPE):
    case FAILURE(ACTION_TYPES.CREATE_SKILLSHAPE):
    case FAILURE(ACTION_TYPES.UPDATE_SKILLSHAPE):
    case FAILURE(ACTION_TYPES.DELETE_SKILLSHAPE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SKILLSHAPE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_SKILLSHAPE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_SKILLSHAPE):
    case SUCCESS(ACTION_TYPES.UPDATE_SKILLSHAPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_SKILLSHAPE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {},
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState,
      };
    default:
      return state;
  }
};

const apiUrl = `${apiPrefix}/api/skill-shapes`;

// Actions

export const getEntities: ICrudGetAllAction<ISkillShape> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_SKILLSHAPE_LIST,
  payload: axios.get<ISkillShape>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<ISkillShape> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_SKILLSHAPE,
    payload: axios.get<ISkillShape>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<ISkillShape> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_SKILLSHAPE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<ISkillShape> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_SKILLSHAPE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<ISkillShape> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_SKILLSHAPE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
