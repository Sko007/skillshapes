import React from 'react';
import { shallow } from 'enzyme';

import sinon from 'sinon';

import LoadingBar from 'react-redux-loading-bar';
import { Navbar, Nav } from 'reactstrap';
import { DashboardMenu } from 'app/shared/layout/menus';
import Header from 'app/shared/layout/header/header';

describe('Dashboard', () => {
  let mountedWrapper;

  const localeSpy = sinon.spy();

  const devProps = {
    isAuthenticated: true,
    isAdmin: true,
    currentLocale: 'en',
    onLocaleChange: localeSpy,
    ribbonEnv: 'dev',
    isInProduction: false,
    isSwaggerEnabled: true,
  };
  const prodProps = {
    ...devProps,
    ribbonEnv: 'prod',
    isInProduction: true,
    isSwaggerEnabled: false,
  };
  const userProps = {
    ...prodProps,
    isAdmin: false,
  };
  const guestProps = {
    ...prodProps,
    isAdmin: false,
    isAuthenticated: false,
  };

  const wrapper = (props = devProps) => {
    if (!mountedWrapper) {
      mountedWrapper = shallow(<Header {...props} />);
    }
    return mountedWrapper;
  };

  beforeEach(() => {
    mountedWrapper = undefined;
  });

  // All tests will go here
  it('Renders a Header component with dashboard menu.', () => {
    const component = wrapper();
    // the created snapshot must be committed to source control
    expect(component).toMatchSnapshot();
    const nav = component.find(Nav);
    expect(nav.find(DashboardMenu).length).toEqual(1);
  });

  it('Renders a Header component in prod profile with logged in User', () => {
    const nav = wrapper(userProps).find(Nav);
    expect(nav.find(DashboardMenu).length).toEqual(1);
  });

  it('Renders no dashboard with no logged in User', () => {
    const nav = wrapper(guestProps).find(Nav);
    expect(nav.find(DashboardMenu).length).toEqual(0);
  });
});
