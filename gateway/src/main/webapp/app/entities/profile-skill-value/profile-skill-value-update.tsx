import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { ISkill } from 'app/shared/model/skill.model';
import { getEntities as getSkills } from 'app/entities/skill/skill.reducer';
import { ISkillShape } from 'app/shared/model/skill-shape.model';
import { getEntities as getSkillShapes } from 'app/entities/skill-shape/skill-shape.reducer';
import { getEntity, updateEntity, createEntity, reset } from './profile-skill-value.reducer';
import { IProfileSkillValue } from 'app/shared/model/profile-skill-value.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IProfileSkillValueUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProfileSkillValueUpdate = (props: IProfileSkillValueUpdateProps) => {
  const [skillId, setSkillId] = useState('0');
  const [skillshapeId, setSkillshapeId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { profileSkillValueEntity, skills, skillShapes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/profile-skill-value');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getSkills();
    props.getSkillShapes();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...profileSkillValueEntity,
        ...values,
      };

      if (isNew) {
        props.createEntity(entity);
      } else {
        props.updateEntity(entity);
      }
    }
  };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="gatewayApp.profileSkillValue.home.createOrEditLabel">
            <Translate contentKey="gatewayApp.profileSkillValue.home.createOrEditLabel">Create or edit a ProfileSkillValue</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : profileSkillValueEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="profile-skill-value-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="profile-skill-value-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="valueLabel" for="profile-skill-value-value">
                  <Translate contentKey="gatewayApp.profileSkillValue.value">Value</Translate>
                </Label>
                <AvField
                  id="profile-skill-value-value"
                  type="string"
                  className="form-control"
                  name="value"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                    min: { value: 1, errorMessage: translate('entity.validation.min', { min: 1 }) },
                    max: { value: 4, errorMessage: translate('entity.validation.max', { max: 4 }) },
                    number: { value: true, errorMessage: translate('entity.validation.number') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="profile-skill-value-skill">
                  <Translate contentKey="gatewayApp.profileSkillValue.skill">Skill</Translate>
                </Label>
                <AvInput id="profile-skill-value-skill" type="select" className="form-control" name="skillId">
                  <option value="" key="0" />
                  {skills
                    ? skills.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.name}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/profile-skill-value" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </AvForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

const mapStateToProps = (storeState: IRootState) => ({
  skills: storeState.skill.entities,
  skillShapes: storeState.skillShape.entities,
  profileSkillValueEntity: storeState.profileSkillValue.entity,
  loading: storeState.profileSkillValue.loading,
  updating: storeState.profileSkillValue.updating,
  updateSuccess: storeState.profileSkillValue.updateSuccess,
});

const mapDispatchToProps = {
  getSkills,
  getSkillShapes,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProfileSkillValueUpdate);
