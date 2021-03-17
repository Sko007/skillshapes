import React, { useState, useEffect } from 'react';
import { connect } from 'react-redux';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Col, Row, Table } from 'reactstrap';
import { Translate, ICrudGetAllAction } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { IRootState } from 'app/shared/reducers';
import { getEntities } from './profile-skill-value.reducer';
import { IProfileSkillValue } from 'app/shared/model/profile-skill-value.model';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';

export interface IProfileSkillValueProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const ProfileSkillValue = (props: IProfileSkillValueProps) => {
  useEffect(() => {
    props.getEntities();
  }, []);

  const { profileSkillValueList, match, loading } = props;
  return (
    <div>
      <h2 id="profile-skill-value-heading">
        <Translate contentKey="gatewayApp.profileSkillValue.home.title">Profile Skill Values</Translate>
        <Link to={`${match.url}/new`} className="btn btn-primary float-right jh-create-entity" id="jh-create-entity">
          <FontAwesomeIcon icon="plus" />
          &nbsp;
          <Translate contentKey="gatewayApp.profileSkillValue.home.createLabel">Create new Profile Skill Value</Translate>
        </Link>
      </h2>
      <div className="table-responsive">
        {profileSkillValueList && profileSkillValueList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th>
                  <Translate contentKey="global.field.id">ID</Translate>
                </th>
                <th>
                  <Translate contentKey="gatewayApp.profileSkillValue.value">Value</Translate>
                </th>
                <th>
                  <Translate contentKey="gatewayApp.profileSkillValue.skill">Skill</Translate>
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {profileSkillValueList.map((profileSkillValue, i) => (
                <tr key={`entity-${i}`}>
                  <td>
                    <Button tag={Link} to={`${match.url}/${profileSkillValue.id}`} color="link" size="sm">
                      {profileSkillValue.id}
                    </Button>
                  </td>
                  <td>{profileSkillValue.value}</td>
                  <td>
                    {profileSkillValue.skillId ? <Link to={`skill/${profileSkillValue.skillId}`}>{profileSkillValue.name}</Link> : ''}
                  </td>
                  <td className="text-right">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`${match.url}/${profileSkillValue.id}`} color="info" size="sm">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profileSkillValue.id}/edit`} color="primary" size="sm">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`${match.url}/${profileSkillValue.id}/delete`} color="danger" size="sm">
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
              <Translate contentKey="gatewayApp.profileSkillValue.home.notFound">No Profile Skill Values found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

const mapStateToProps = ({ profileSkillValue }: IRootState) => ({
  profileSkillValueList: profileSkillValue.entities,
  loading: profileSkillValue.loading,
});

const mapDispatchToProps = {
  getEntities,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(ProfileSkillValue);
