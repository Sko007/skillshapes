import React from 'react';
import { Translate } from 'react-jhipster';

import { NavItem, NavLink, NavbarBrand } from 'reactstrap';
import { NavLink as Link } from 'react-router-dom';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import appConfig from 'app/config/constants';

export const BrandIcon = props => (
  <div {...props} className="brand-icon">
    <SVG></SVG>
  </div>
);

export const Brand = props => (
  <NavbarBrand tag={Link} to="/" className="brand-logo">
    <BrandIcon />
    <span className="brand-title">
      <Translate contentKey="global.title">Skillshapes</Translate>
    </span>
    <span className="navbar-version">{appConfig.VERSION}</span>
  </NavbarBrand>
);

export const Home = props => (
  <NavItem>
    <NavLink tag={Link} to="/" className="d-flex align-items-center">
      <FontAwesomeIcon icon="home" />
      <span>
        <Translate contentKey="global.menu.home">Home</Translate>
      </span>
    </NavLink>
  </NavItem>
);
export const SVG = props => (
  <svg
  version="1.0"
  xmlns="http://www.w3.org/2000/svg"
  width="40.000000pt"
  height="40.000000pt"
  viewBox="0 0 200.000000 200.000000"
  preserveAspectRatio="xMidYMid meet"
>
<g
  transform="translate(0.000000,200.000000) scale(0.100000,-0.100000)"
  fill="#FFFFFF"
  stroke="none"
>
  <path
    d="M889 1950 c-345 -43 -645 -271 -774 -588 -75 -184 -90 -409 -41 -597
    89 -340 351 -602 691 -691 143 -37 327 -37 470 0 340 89 602 351 691 691 37
    143 37 327 0 470 -106 407 -458 695 -876 718 -52 3 -125 2 -161 -3z m391 -480
    l0 -170 180 0 180 0 0 -115 0 -115 -180 0 -180 0 0 -335 0 -335 -269 0 c-240
    0 -276 2 -333 20 -130 40 -228 136 -270 264 -31 96 -31 252 1 341 30 84 83
    157 144 199 85 58 126 69 267 70 l125 1 21 -75 c12 -41 25 -94 29 -117 l7 -43
    -115 0 c-143 0 -187 -19 -226 -95 -35 -67 -35 -163 0 -230 40 -78 82 -95 241
    -95 l128 0 0 500 0 500 125 0 125 0 0 -170z"
    />
</g>
</svg>
    
    );