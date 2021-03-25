import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from 'app/entities/skill-shape/skill-shape.reducer';
import { ISkillShape } from 'app/shared/model/skill-shape.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { AdminContext } from 'app/app';

interface ISkillShapeDashboardProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {
  isAuthenticated: boolean;
}

export const SkillShapeDashboard = (props: ISkillShapeDashboardProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { skillShapeList, match, loading, isAuthenticated } = props;
  return (
    <AdminContext.Consumer>
      {isAdmin => (
        <div>
          <h2 id="skill-shape-heading">
            <Translate contentKey="gatewayApp.skillShape.home.title">Skill Shapes</Translate>
            {isAuthenticated && isAdmin && (
              <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
                <FontAwesomeIcon icon="plus" />
                &nbsp;
                <Translate contentKey="gatewayApp.skillShape.home.createLabel">Create new Skill Shape</Translate>
              </Link>
            )}
          </h2>
          <div className="table-responsive">
            {skillShapeList && skillShapeList.length > 0 ? (
              <Table responsive>
                <thead>
                  <tr>
                    <th>
                      <Translate contentKey="global.field.id">ID</Translate>
                    </th>
                    <th>
                      <Translate contentKey="gatewayApp.skillShape.title">Title</Translate>
                    </th>
                    <th>
                      <Translate contentKey="gatewayApp.skillShape.category">Category</Translate>
                    </th>
                    <th>
                      <Translate contentKey="gatewayApp.skillShape.skill">Skill</Translate>
                    </th>
                    <th>
                      <Translate contentKey="gatewayApp.skillShape.owner">Owner</Translate>
                    </th>
                    <th />
                  </tr>
                </thead>
                <tbody>
                  {skillShapeList.map((skillShape, i) => (
                    <tr key={`entity-${i}`}>
                      <td>
                        <Button tag={Link} to={`${match.url}/${skillShape.id}`} color="link" size="sm">
                          {skillShape.id}
                        </Button>
                      </td>
                      <td>
                        <Button tag={Link} to={`${match.url}/${skillShape.id}`} color="link" size="sm">
                          {skillShape.title}
                        </Button>
                      </td>
                      <td>{skillShape.category}</td>
                      <td>
                        {skillShape.skills
                          ? skillShape.skills.map((val, j) => (
                              <span key={j}>
                                <Link to={`profile-skill-value/${val.id}`}>{val.name}</Link>
                                {j === skillShape.skills.length - 1 ? '' : ', '}
                              </span>
                            ))
                          : null}
                      </td>
                      <td>
                        {skillShape.owners
                          ? skillShape.owners.map((val, j) => (
                              <span key={j}>
                                <Link to={`user-profile/${val.id}`}>{`${val.firstName} ${val.lastName} <${val.email}>`}</Link>
                                {j === skillShape.owners.length - 1 ? '' : ', '}
                              </span>
                            ))
                          : null}
                      </td>
                      <td className="text-right">
                        <div className="btn-group flex-btn-group-container">
                          <Button tag={Link} to={`${match.url}/${skillShape.id}`} color="info" size="sm">
                            <FontAwesomeIcon icon="eye" />{' '}
                          </Button>
                          <Button tag={Link} to={`${match.url}/${skillShape.id}/edit`} color="primary" size="sm">
                            <FontAwesomeIcon icon="pencil-alt" />{' '}
                          </Button>
                          <Button tag={Link} to={`${match.url}/${skillShape.id}/delete`} color="danger" size="sm">
                            <FontAwesomeIcon icon="trash" />{' '}
                          </Button>
                        </div>
                      </td>
                    </tr>
                  ))}
                </tbody>
              </Table>
            ) : (
              !loading && (
                <div className="alert alert-warning">
                  <Translate contentKey="gatewayApp.skillShape.home.notFound">No Skill Shapes found</Translate>
                </div>
              )
            )}
          </div>
        </div>
      )}
    </AdminContext.Consumer>
  );
};

const mapStateToProps = ({ skillShape, authentication }: IRootState) => ({
  skillShapeList: skillShape.entities,
  loading: skillShape.loading,
  isAuthenticated: authentication.isAuthenticated,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SkillShapeDashboard);
