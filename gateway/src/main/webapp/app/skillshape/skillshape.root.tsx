import React, { useState, useEffect, createContext, useRef } from 'react';
import { connect } from 'react-redux';
import { RouteComponentProps } from 'react-router-dom';
import { getUserData, getTechnologies } from './skillshape.reducer';
import SkillshapeWrapper from './skillshape.wrapper';
import { Button } from "reactstrap"

import "./skillshapes.scss"

import { IRootState } from 'app/shared/reducers';

const centerTheDiv = {
  display: 'flex',
  justifyContent: 'center',
  width: '100%',
  height: "100%",
};
// export interface ISkillshapes
export const SkillshapeData = createContext([]);
export interface ISkillshapesProps extends StateProps, DispatchProps, RouteComponentProps<{ url: string }> {}

export const SkillshapeRoot = (props: ISkillshapesProps): any => {
  useEffect(() => {
    props.getUserData();
    props.getTechnologies();
  }, []);
  const [button, setButton] = useState(true)
  const overlay = useRef(null)
  const overlay1 = useRef(null)


const effect = () => {
  overlay.current.className = "overlay open1";
  overlay1.current.className = "overlay-1 open";
}


  const { user, category, title, level2 } = props;

  if (user === undefined || category === undefined || title === undefined || level2 === undefined) {
    return 'loading';
  }
  return (
    <>
      <SkillshapeData.Provider value={[user, category, title, level2]}>
        
 
        <div className="fixed-div">
        <div className="wrapper">
          
           {button && <Button onClick={():void => {effect(), setButton(false)}} className="overlay-button" color="secondary" size="lg" >Öffnen</Button> } 
           
            <div ref={overlay} className="overlay-1">
              <h1 className="headline">Skillshapes 
              </h1>
              <hr></hr>
               <h3 className="headline1">{user.firstName}, {user.lastName}</h3>
              <h4 className="fonts">- Im Horizontalen Bereich steht der Background des Users</h4>
              <h4 className="fonts1">- Im Vertikalen Bereich seine Skills</h4>
              <h4 className="fonts2">- Im linken Bildschirmrand ist eine Navigationsleite</h4>
          </div>
          <div ref={overlay1} className="overlay">
          <img className="image" src="content/images/default_bild.jpg" alt="Logo" />
          </div>
        </div>
        <div style={centerTheDiv}>
          {!button && <SkillshapeWrapper></SkillshapeWrapper>}
          </div>
            
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
