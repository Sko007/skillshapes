import React from 'react';
import { Translate } from 'react-jhipster';
import { Col, Row } from 'reactstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faStar } from '@fortawesome/free-solid-svg-icons';

const Rating = props => (
  <div className="rating star-rating">
    {props.value &&
      [...Array(4)].map((x, i) => {
        return <FontAwesomeIcon size="xs" icon={faStar} color={props.value > i ? '#0090FF' : '#a9a9a9'} />;
      })}
  </div>
);

export default Rating;
