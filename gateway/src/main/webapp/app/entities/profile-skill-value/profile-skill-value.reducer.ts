import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IProfileSkillValue, defaultValue } from 'app/shared/model/profile-skill-value.model';

export const ACTION_TYPES = {
  FETCH_PROFILESKILLVALUE_LIST: 'profileSkillValue/FETCH_PROFILESKILLVALUE_LIST',
  FETCH_PROFILESKILLVALUE: 'profileSkillValue/FETCH_PROFILESKILLVALUE',
  CREATE_PROFILESKILLVALUE: 'profileSkillValue/CREATE_PROFILESKILLVALUE',
  UPDATE_PROFILESKILLVALUE: 'profileSkillValue/UPDATE_PROFILESKILLVALUE',
  DELETE_PROFILESKILLVALUE: 'profileSkillValue/DELETE_PROFILESKILLVALUE',
  RESET: 'profileSkillValue/RESET',
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IProfileSkillValue>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false,
};

export type ProfileSkillValueState = Readonly<typeof initialState>;

// Reducer

export default (state: ProfileSkillValueState = initialState, action): ProfileSkillValueState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_PROFILESKILLVALUE_LIST):
    case REQUEST(ACTION_TYPES.FETCH_PROFILESKILLVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true,
      };
    case REQUEST(ACTION_TYPES.CREATE_PROFILESKILLVALUE):
    case REQUEST(ACTION_TYPES.UPDATE_PROFILESKILLVALUE):
    case REQUEST(ACTION_TYPES.DELETE_PROFILESKILLVALUE):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true,
      };
    case FAILURE(ACTION_TYPES.FETCH_PROFILESKILLVALUE_LIST):
    case FAILURE(ACTION_TYPES.FETCH_PROFILESKILLVALUE):
    case FAILURE(ACTION_TYPES.CREATE_PROFILESKILLVALUE):
    case FAILURE(ACTION_TYPES.UPDATE_PROFILESKILLVALUE):
    case FAILURE(ACTION_TYPES.DELETE_PROFILESKILLVALUE):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROFILESKILLVALUE_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.FETCH_PROFILESKILLVALUE):
      return {
        ...state,
        loading: false,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.CREATE_PROFILESKILLVALUE):
    case SUCCESS(ACTION_TYPES.UPDATE_PROFILESKILLVALUE):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data,
      };
    case SUCCESS(ACTION_TYPES.DELETE_PROFILESKILLVALUE):
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

const apiUrl = 'api/profile-skill-values';

// Actions

export const getEntities: ICrudGetAllAction<IProfileSkillValue> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_PROFILESKILLVALUE_LIST,
  payload: axios.get<IProfileSkillValue>(`${apiUrl}?cacheBuster=${new Date().getTime()}`),
});

export const getEntity: ICrudGetAction<IProfileSkillValue> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_PROFILESKILLVALUE,
    payload: axios.get<IProfileSkillValue>(requestUrl),
  };
};

export const createEntity: ICrudPutAction<IProfileSkillValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_PROFILESKILLVALUE,
    payload: axios.post(apiUrl, cleanEntity(entity)),
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IProfileSkillValue> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_PROFILESKILLVALUE,
    payload: axios.put(apiUrl, cleanEntity(entity)),
  });
  return result;
};

export const deleteEntity: ICrudDeleteAction<IProfileSkillValue> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_PROFILESKILLVALUE,
    payload: axios.delete(requestUrl),
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET,
});
