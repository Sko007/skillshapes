import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from './skill-shape.reducer';
import { ISkillShape } from 'app/shared/model/skill-shape.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISkillShapeDetailProps extends StateProps, DispatchProps, RouteComponentProps<{ id: string }> {}

export const SkillShapeDetail = (props: ISkillShapeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { skillShapeEntity } = props;
  return (
    <Row>
      <Col md="8">
        <h2>
          <Translate contentKey="gatewayApp.skillShape.detail.title">SkillShape</Translate> [<b>{skillShapeEntity.id}</b>]
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="title">
              <Translate contentKey="gatewayApp.skillShape.title">Title</Translate>
            </span>
          </dt>
          <dd>{skillShapeEntity.title}</dd>
          <dt>
            <span id="category">
              <Translate contentKey="gatewayApp.skillShape.category">Category</Translate>
            </span>
          </dt>
          <dd>{skillShapeEntity.category}</dd>
          <dt>
            <Translate contentKey="gatewayApp.skillShape.skill">Skill</Translate>
          </dt>
          <dd>
            {skillShapeEntity.skills
              ? skillShapeEntity.skills.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {skillShapeEntity.skills && i === skillShapeEntity.skills.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
          <dt>
            <Translate contentKey="gatewayApp.skillShape.owner">Owner</Translate>
          </dt>
          <dd>
            {skillShapeEntity.owners
              ? skillShapeEntity.owners.map((val, i) => (
                  <span key={val.id}>
                    <a>{val.id}</a>
                    {skillShapeEntity.owners && i === skillShapeEntity.owners.length - 1 ? '' : ', '}
                  </span>
                ))
              : null}
          </dd>
        </dl>
        <Button tag={Link} to="/skill-shape" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/skill-shape/${skillShapeEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

const mapStateToProps = ({ skillShape }: IRootState) => ({
  skillShapeEntity: skillShape.entity,
});

const mapDispatchToProps = { getEntity };

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SkillShapeDetail);
