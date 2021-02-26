import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './profile-skill-value.reducer';
import { IProfileSkillValue } from 'app/shared/model/profile-skill-value.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfileSkillValueDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const ProfileSkillValueDetail = (props: IProfileSkillValueDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { profileSkillValueEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="gatewayApp.profileSkillValue.detail.title">ProfileSkillValue</Translate> [
          <b>{profileSkillValueEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="value">
              <Translate contentKey="gatewayApp.profileSkillValue.value">Value</Translate>
            </span>
          </dt>
          <dd>{profileSkillValueEntity.value}</dd>
          <dt>
            <Translate contentKey="gatewayApp.profileSkillValue.skill">Skill</Translate>
          </dt>
          <dd>{profileSkillValueEntity.skill ? profileSkillValueEntity.skill.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/profile-skill-value" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/profile-skill-value/${profileSkillValueEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ profileSkillValue }: IRootState) => ({
  profileSkillValueEntity: profileSkillValue.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProfileSkillValueDetail);
