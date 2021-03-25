import React, { useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate, ICrudGetAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntity } from 'app/entities/skill-shape/skill-shape.reducer';
import { ISkillShape } from 'app/shared/model/skill-shape.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ISkillShapeDetailProps } from 'app/entities/skill-shape/skill-shape-detail';
import Rating from 'app/shared/layout/rating/rating';

export const SkillShapeDashboardDetail = (props: ISkillShapeDetailProps) => {
  useEffect(() => {
    props.getEntity(props.match.params.id);
  }, []);

  const { skillShapeEntity } = props;
  return (
    <Row>
      <Col className="mt-5" sm="12" md={{ size: 4, offset: 4 }}>
        <dl className="jh-entity-details card">
          <dd>
            <h2 className="card-title">{skillShapeEntity.title}</h2>
            <h5 className="text-muted">{skillShapeEntity.category}</h5>
            <h5>
              <dd>
                {skillShapeEntity.owners
                  ? skillShapeEntity.owners.map((val, i) => (
                      <a color="link" className="link" href={`mailto:${val.email}`} key={val.id}>
                        {`${val.firstName} ${val.lastName} <${val.email}>`}
                        {skillShapeEntity.owners && i === skillShapeEntity.owners.length - 1 ? '' : ', '}
                      </a>
                    ))
                  : null}
              </dd>
            </h5>
          </dd>
          <hr className="my-2" />
          <dd>
            <ul className="list-group list-group-flush">
              {skillShapeEntity.skills
                ? skillShapeEntity.skills.map((val, i) => (
                    <li className="list-group-item d-flex justify-content-between align-items-center" key={val.id}>
                      <span area-label={val.name}>{val.name}</span>
                      <Rating value={val.value}></Rating>
                      {/* {skillShapeEntity.skills && i === skillShapeEntity.skills.length - 1 ? '' : ', ' } */}
                    </li>
                  ))
                : null}
            </ul>
          </dd>
        </dl>
        <Button tag={Link} to="/dashboard/" replace color="info">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/dashboard/${skillShapeEntity.id}/edit`} replace color="primary">
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

export default connect(mapStateToProps, mapDispatchToProps)(SkillShapeDashboardDetail);
