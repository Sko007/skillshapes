import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './skill.reducer';
import { ISkill } from 'app/shared/model/skill.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface ISkillProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const Skill = (props: ISkillProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { skillList, match, loading } = props;
  return (
    <div>
      <h2 id="skill-heading">
        <Translate contentKey="gatewayApp.skill.home.title">Skills</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="gatewayApp.skill.home.createLabel">Create new Skill</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {skillList && skillList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="gatewayApp.skill.name">Name</Translate>
                </th>
                <th>
                  <Translate contentKey="gatewayApp.skill.categoryName">Category Name</Translate>
                </th>
                <th>
                  <Translate contentKey="gatewayApp.skill.owner">Owner</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {skillList.map((skill, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${skill.id}`} color="link" size="sm">
                      {skill.id}
                    </Button>
                  </td>
                  <td>{skill.name}</td>
                  <td>{skill.categoryName}</td>
                  <td>
                    {skill.owners
                      ? skill.owners.map((val, j) => (
                          <span key={j}>
                            <Link to={`user-profile/${val.id}`}>{val.id}</Link>
                            {j === skill.owners.length - 1 ? '' : ', '}
                          </span>
                        ))
                      : null}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${skill.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${skill.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${skill.id}/delete`} color="danger" size="sm">
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
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
              <Translate contentKey="gatewayApp.skill.home.notFound">No Skills found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ skill }: IRootState) => ({
  skillList: skill.entities,
  loading: skill.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(Skill);
