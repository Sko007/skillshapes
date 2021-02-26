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
import { getEntity, updateEntity, createEntity, reset } from './user-profile.reducer';
import { IUserProfile } from 'app/shared/model/user-profile.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';

export interface IUserProfileUpdateProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const UserProfileUpdate = (props: IUserProfileUpdateProps) => {
  const [skillId, setSkillId] = useState('0');
  const [skillshapeId, setSkillshapeId] = useState('0');
  const [isNew, setIsNew] = useState(!props.match.params || !props.match.params.id);

  const { userProfileEntity, skills, skillShapes, loading, updating } = props;

  const handleClose = () => {
    props.history.push('/user-profile');
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
        ...userProfileEntity,
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
          <h2 id="gatewayApp.userProfile.home.createOrEditLabel">
            <Translate contentKey="gatewayApp.userProfile.home.createOrEditLabel">Create or edit a UserProfile</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <AvForm model={isNew ? {} : userProfileEntity} onSubmit={saveEntity}>
              {!isNew ? (
                <AvGroup>
                  <Label for="user-profile-id">
                    <Translate contentKey="global.field.id">ID</Translate>
                  </Label>
                  <AvInput id="user-profile-id" type="text" className="form-control" name="id" required readOnly />
                </AvGroup>
              ) : null}
              <AvGroup>
                <Label id="firstNameLabel" for="user-profile-firstName">
                  <Translate contentKey="gatewayApp.userProfile.firstName">First Name</Translate>
                </Label>
                <AvField id="user-profile-firstName" type="text" name="firstName" />
              </AvGroup>
              <AvGroup>
                <Label id="lastNameLabel" for="user-profile-lastName">
                  <Translate contentKey="gatewayApp.userProfile.lastName">Last Name</Translate>
                </Label>
                <AvField id="user-profile-lastName" type="text" name="lastName" />
              </AvGroup>
              <AvGroup>
                <Label id="emailLabel" for="user-profile-email">
                  <Translate contentKey="gatewayApp.userProfile.email">Email</Translate>
                </Label>
                <AvField id="user-profile-email" type="text" name="email" />
              </AvGroup>
              <AvGroup>
                <Label id="generalKnowledgeLabel" for="user-profile-generalKnowledge">
                  <Translate contentKey="gatewayApp.userProfile.generalKnowledge">General Knowledge</Translate>
                </Label>
                <AvField id="user-profile-generalKnowledge" type="text" name="generalKnowledge" />
              </AvGroup>
              <Button tag={Link} id="cancel-save" to="/user-profile" replace color="info">
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
  userProfileEntity: storeState.userProfile.entity,
  loading: storeState.userProfile.loading,
  updating: storeState.userProfile.updating,
  updateSuccess: storeState.userProfile.updateSuccess,
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

export default connect(mapStateToProps, mapDispatchToProps)(UserProfileUpdate);
