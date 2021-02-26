import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, Label } from 'reactstrap';
import { AvFeedback, AvForm, AvGroup, AvInput, AvField } from 'availity-reactstrap-validation';
import { Translate, translate, ICrudGetAction, ICrudGetAllAction, ICrudPutAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { IRootState } from 'app/shared/reducers';

import { IProfileSkillValue } from 'app/shared/model/profile-skill-value.model';
import { getEntities as getProfileSkillValues } from 'app/entities/profile-skill-value/profile-skill-value.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { getEntities as getUserProfiles } from 'app/entities/user-profile/user-profile.reducer';
import { getEntity, updateEntity, createEntity, reset } from './skill-shape.reducer';
import { ISkillShape } from 'app/shared/model/skill-shape.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface ISkillShapeUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SkillShapeUpdate = (props: ISkillShapeUpdateProps) => {
  const [idsskill, setIdsskill] = useState([]);
  const [idsowner, setIdsowner] = useState([]);
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { skillShapeEntity, profileSkillValues, userProfiles, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/skill-shape');
  };

  useEffect(() => {
    if (isNew) {
      props.reset();
    } else {
      props.getEntity(props.match.params.id);
    }

    props.getProfileSkillValues();
    props.getUserProfiles();
  }, []);

  useEffect(() => {
    if (props.updateSuccess) {
      handleClose();
    }
  }, [props.updateSuccess]);

  const saveEntity = (event, errors, values) => {
    if (errors.length === 0) {
      const entity = {
        ...skillShapeEntity,
        ...values,
        skills: mapIdList(values.skills),
        owners: mapIdList(values.owners),
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
          <h2 id="gatewayApp.skillShape.home.createOrEditLabel">
            <Translate contentKey="gatewayApp.skillShape.home.createOrEditLabel">Create or edit a SkillShape</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : skillShapeEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="skill-shape-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="skill-shape-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="titleLabel" for="skill-shape-title">
                  <Translate contentKey="gatewayApp.skillShape.title">Title</Translate>
                </Label>
                <AvField
                  id="skill-shape-title"
                  type="text"
                  name="title"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label id="categoryLabel" for="skill-shape-category">
                  <Translate contentKey="gatewayApp.skillShape.category">Category</Translate>
                </Label>
                <AvField
                  id="skill-shape-category"
                  type="text"
                  name="category"
                  validate={{
                    required: { value: true, errorMessage: translate('entity.validation.required') },
                  }}
                />
              </AvGroup>
              <AvGroup>
                <Label for="skill-shape-skill">
                  <Translate contentKey="gatewayApp.skillShape.skill">Skill</Translate>
                </Label>
                <AvInput
                  id="skill-shape-skill"
                  type="select"
                  multiple
                  className="form-control"
                  name="skills"
                  value={skillShapeEntity.skills && skillShapeEntity.skills.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {profileSkillValues
                    ? profileSkillValues.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <AvGroup>
                <Label for="skill-shape-owner">
                  <Translate contentKey="gatewayApp.skillShape.owner">Owner</Translate>
                </Label>
                <AvInput
                  id="skill-shape-owner"
                  type="select"
                  multiple
                  className="form-control"
                  name="owners"
                  value={skillShapeEntity.owners && skillShapeEntity.owners.map(e => e.id)}
                >
                  <option value="" key="0" />
                  {userProfiles
                    ? userProfiles.map(otherEntity => (
                        <option value={otherEntity.id} key={otherEntity.id}>
                          {otherEntity.id}
                        </option>
                      ))
                    : null}
                </AvInput>
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/skill-shape" replace color="info">
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
  profileSkillValues: storeState.profileSkillValue.entities,
  userProfiles: storeState.userProfile.entities,
  skillShapeEntity: storeState.skillShape.entity,
  loading: storeState.skillShape.loading,
  updating: storeState.skillShape.updating,
  updateSuccess: storeState.skillShape.updateSuccess,
});

const mapDispatchToProps = {
  getProfileSkillValues,
  getUserProfiles,
  getEntity,
  updateEntity,
  createEntity,
  reset,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SkillShapeUpdate);
