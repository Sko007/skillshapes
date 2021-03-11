import React, { useState, useEffect, createContext } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { getUserData, getTechnologies } from './skillshape.reducer';
import SkillshapeWrapper from './skillshape.wrapper';

import { IRootState } from 'app/shared/reducers';

const centerTheDiv = {
  display: 'flex',
  justifyContent: 'center',
  width: '100%',
  height: '100%',
};
// export interface ISkillshapes
export const SkillshapeData = createContext([]);
export interface ISkillshapesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SkillshapeRoot = (props: ISkillshapesProps): any => {
  useEffect(() => {
    props.getUserData();
    props.getTechnologies();
  }, []);

  const { user, technologies, category, title, level2 } = props;
  // eslint-disable-next-line no-console
  console.log(
    `check the user in root ${JSON.stringify(user)} and ${JSON.stringify(category)} title ${JSON.stringify(title)}level2: ${JSON.stringify(
      level2
    )}`
  );
  if (user === undefined || category === undefined || title === undefined || level2 === undefined) {
    return 'loading';
  }
  return (
    <>
      <SkillshapeData.Provider value={[user, category, title, level2]}>
        <div style={centerTheDiv}>
          <SkillshapeWrapper></SkillshapeWrapper>
        </div>
      </SkillshapeData.Provider>
    </>
  );
};
const mapStateToProps = ({ skillshapes }: IRootState) => ({
  user: skillshapes.entity,
  technologies: skillshapes.technologies,
  category: skillshapes.category,
  title: skillshapes.title,
  level2: skillshapes.level2,
});

const mapDispatchToProps = {
  getUserData,
  getTechnologies,
};

type StateProps = ReturnType<typeof mapStateToProps>;
type DispatchProps = typeof mapDispatchToProps;

export default connect(mapStateToProps, mapDispatchToProps)(SkillshapeRoot);
