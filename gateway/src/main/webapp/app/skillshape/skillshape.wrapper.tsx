import React, { useRef, useState, useEffect, useContext } from 'react';
import { SkillshapeData } from './skillshape.root';
import * as d3 from 'd3';
import './skillshapes.scss';
import * as Logo from "./devoteam-200x200.png"
import { useHistory } from 'react-router-dom';

const SkillshapeWrapper = () => {
  const history = useHistory();
  const skillshapeData = useContext(SkillshapeData);
  const [headline, setHeadline] = useState(skillshapeData[1][1].name);
  const [color, setColor] = useState(true);
  const [buttonId, setButtonId] = useState(0);
  const buttonRef = useRef([]);
  const [bars, setBars] = useState(skillshapeData[2]);

  const onClick = (d: any, i: any) => {};
  const Level1 = () => {
    setColor(true);
    buttonRef.current[buttonId].style.backgroundColor = '';
    setHeadline(skillshapeData[1][1].name);
    setBars(skillshapeData[2]);

    history.push({
      pathname: '/',
      search: 'level1',
      state: bars,
    });
  };
  const Level2 = (id: number, skillId: number) => {
    setBars(skillshapeData[3][id]);
    buttonRef.current[buttonId].style.backgroundColor = '';
    setColor(false);
    setHeadline(skillshapeData[2][id].name);
    setButtonId(id);
    buttonRef.current[id].style.backgroundColor = '#f7485e';

    history.push({
      pathname: '/',
      search: 'level2',
      state: bars,
    });
  };

  const sortForNumbers = () => {
    const addKey = bars.map(item => {
      return { ...item, new: 3 };
    });
    const getValues = addKey.sort((element: any, element2: any) => element2.value - element.value);
    setBars(getValues);
  };

  const sortForAlphabeth = () => {
    const addKey = bars.map(item => {
      return { ...item, new: 3 };
    });

    const getSortedNames = addKey.sort(function (item: any, item2: any) {
      return item.name.toLowerCase().localeCompare(item2.name.toLowerCase());
    });
    setBars(getSortedNames);
  };

  const margin = { top: 30, right: 50, bottom: 0, left: 50 };
  const WIDTH = window.innerWidth - window.innerWidth / 4 - margin.left - margin.right;
  const HEIGHT = 600 - margin.top - margin.bottom;

  useEffect(() => {
    const svg = d3.select('#skillshape').attr('width', WIDTH + margin.left + margin.right);
    const y = d3.scaleLinear().domain([5, 0]).range([HEIGHT, 0]).nice();
    const x = d3
      .scaleBand()
      .domain(bars.map((item: any) => item.name))
      .range([0, WIDTH])
      .paddingInner(0.2)
      .paddingOuter(0.2);

    /**
     * JOIN, Data to the g-element and passes it to the g-element
     */
    const myGroups = d3.select('#skillshape').selectAll('g').data(bars);

    const groupEnter = myGroups.enter().append('g');

    /**
     * ENTER, Append Bars to Data
     */
    groupEnter
      .append('rect')
      .attr('x', (d: any) => x(d.name))
      .attr('width', x.bandwidth)
      .attr('fill', '#C6C6C6')
      .on('mouseover', function () {
        d3.select(this).attr('opacity', '0.5');
      })
      .on('mouseout', function () {
        d3.select(this).attr('opacity', '1');
      })
      .on('click', (d: any, i: any) => {
        onClick(i.id, d);
      })
      .transition()
      .duration(500)
      .attr('height', (d: any) => y(d.value))
      .attr('y', d => 0);
    /**
     * Enter, Append Text to Data
     */
    groupEnter
      .append('text')
      .attr('display', 'flex')
      .attr('justify-content', 'center')
      .attr('font-weight', 600)
      .attr('font-family', '-apple-system')
      .attr('font-size', '20px')

      .attr('text-anchor', 'middle')
      .attr('x', function (d: any) {
        return x(d.name) + x.bandwidth() / 2;
      })

      .transition()
      .duration(500)
      .attr('y', function (d: any) {
        return y(d.value) / 2;
      })
      .attr('dy', '.75em')
      .text(function (d: any) {
        return d.name;
      });

    /**
     * Update, select text element and pass parts which should be updated
     */
    /**
     * Update, Rects elements and pass pars which should be updated
     */
    myGroups
      .select('rect')
      .transition()
      .duration(500)
      .attr('width', x.bandwidth)
      .attr('height', (d: any) => y(d.value))
      .attr('x', (d: any) => x(d.name))
      .attr('y', d => 0);

    myGroups
      .select('text')
      .transition()
      .duration(500)
      .text(function (d: any) {
        return d.name;
      })
      .attr('text-anchor', 'middle')
      .attr('x', function (d: any) {
        return x(d.name) + x.bandwidth() / 2;
      })
      .attr('y', function (d: any) {
        return y(d.value) / 2;
      })
      .attr('dy', '.75em');
    /**
     * Exit old data from Datasource for Rects
     */
    myGroups
      .exit()
      .attr('height', 0)
      .attr('y', HEIGHT)
      .attr('width', x.bandwidth)
      .attr('x', (d: any) => x(d.name))
      .transition()
      .duration(500)
      .attr('height', (d: any) => d.value)
      .attr('y', (d: any) => x(d.value))
      .remove();
    /**
     * Exit old data from Datasource for Text
     */
    myGroups
      .exit()
      .transition()
      .duration(500)
      // Text
      .text(function (d: any) {
        return d.name;
      })
      .attr('y', function (d: any) {
        return y(d.value) / 2;
      })
      .attr('text-anchor', 'middle')
      .attr('x', function (d: any) {
        return x(d.name) + x.bandwidth() / 2;
      })
      .attr('dy', '.75em')
      .remove();
  });
  return (
    <div
      style={{
        display: 'flex',
        justifyContent: 'space-between',
        flexDirection: 'row',
        width: '100%',
        minHeight: '100%',
        alignItems: 'center',
        flexWrap: 'wrap',
      }}
    >
      <div
        style={{
          display: 'flex',
          alignItems: 'center',
          width: '13vw',
          minWidth: '200px',
          height: '100%',
          backgroundColor: '#636363',
          boxSizing: 'border-box',
          borderRadius: '20px 20px 20px 20px',
        }}
      >
        <div style={{ display: 'grid', height: '100%', gridTemplateRows: '20% 60% 20' }}>
          <div style={{ display: 'flex', flexDirection: 'row', marginTop: '3%' }}>
            <img style={{ height: '50px', width: '50px', paddingRight: '5px' }} src={String(Logo)}></img>

            <h3 style={{ paddingTop: '3%', color: 'white' }}>
              {skillshapeData[0].firstName} {skillshapeData[0].lastName}
            </h3>
          </div>
          <div>
            <ul className="list" style={{ fontSize: '24px', display: 'block' }}>
              <button style={{ backgroundColor: color ? '#f7485e' : '', width: '100%' }} id="button1" onClick={Level1}>
                Übersicht
              </button>
              <hr></hr>
              {skillshapeData[2].map((item: any, i: number) => {
                return (
                  <button
                    key={i}
                    ref={ref => buttonRef.current.push(ref)}
                    id={'button' + i}
                    style={{ width: '100%' }}
                    onClick={(): void => Level2(i, item.id)}
                  >
                    {item.name}
                  </button>
                );
              })}
            </ul>
          </div>
          <div style={{ display: 'flex', alignItems: 'flex-End' }}>
            <div style={{ display: 'block' }}>
              <button style={{ width: '100%' }} onClick={sortForNumbers}>
                Sortieren nach Können
              </button>
              <button style={{ width: '100%' }} onClick={sortForAlphabeth}>
                Sortieren nach Alphabeth
              </button>
            </div>
          </div>
        </div>
      </div>
      <div>
        <div
          style={{
            display: 'flex',
            justifyContent: 'center',
            alignItems: 'center',
            width: WIDTH,
            height: '100%',
            backgroundColor: '#f7485e',
            padding: '3.4%',
            boxSizing: 'border-box',
            borderRadius: '20px 20px 20px 20px',
          }}
        >
          <h1 style={{ color: 'white' }}>{headline}</h1>
        </div>
        <div></div>
        <svg id="skillshape" style={{ height: HEIGHT, width: WIDTH }}></svg>
      </div>
    </div>
  );
};

export default SkillshapeWrapper;
