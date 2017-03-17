'use strict'

import {Label, OverlayTrigger, Button, Badge} from 'react-bootstrap';

const React = require('react');
const srcPath = '/src/attach/';
const thumbPath = srcPath+'thumbs/';

export const СHeader = ({message, controls}) => (
  <div className="message-header">
      <span className="message-title">{message.title}</span>&nbsp;
      <Label bsStyle="success">Anonymous</Label>&nbsp;
      <Label bsStyle="info">{new Date(message.timestamp).toLocaleString()}</Label>&nbsp;
      <a name={message.id} href={'#'+message.id}>{'№'+message.id}</a>&nbsp;
      {controls}
  </div>
)
export const CThreadCtrl = ({thread}) => {
  let badgeCount = thread.count-3 > 0 ? thread.count-3 : 0;
  return(<span>
    <Button bsSize="xsmall" bsStyle="default">-</Button>&nbsp;
    <Button bsSize="xsmall" bsStyle="primary" href={window.location.pathname + "/thread/" + thread.id}>Open</Button>
    {badgeCount > 0 ? <Badge title="Omitted replies" >{badgeCount}</Badge> : null}
  </span>
)}
export const CThumbs = ({attachments, onThumbClick}) => {
  let thumbs = <div/>;
  if (attachments.length > 1) {
    thumbs = attachments.map(attach =>
      <div key={attach.name} className="thumb-box item"><img className="thumb" src={thumbPath+attach.name} id={attach.name} alt="Image" onClick={onThumbClick}/></div>
    );
  }else {
    if (attachments.length > 0) thumbs = <img className="thumb" src={thumbPath+attachments[0].name} id={attachments[0].name} alt="Image" onClick={onThumbClick}/>
  }
  return(
      <div className="thumb-box">{thumbs}</div>
)}
export const CPopover = ({threadId, messageId, render}) => {
  return(
    <OverlayTrigger rootClose trigger={['hover']} delayHide={5000} overlay={render(threadId, messageId)}>
      <a href={'#'+messageId}>{'>>'+messageId}</a>
    </OverlayTrigger>
)}
export const СMessage = ({message, controls, style, replies}) => {
  let repl = [];
  for (var key in replies) {
    repl.push(replies[key]);
  }
  return(
    <div className="post-wrapper">
      <div className={style}>
        <СHeader message={message} controls={controls}/>
        <div className="message-body">
          {message.thumbs}
          <blockquote className="message-text">{message.text}</blockquote>
        </div>
        {repl.length > 0 ? <div className="replies">Replies: {repl}</div> : null}
      </div>
    </div>
)}
export const СThread = ({thread, replies}) => {
  let messages = thread.messages.map(message =>
    <СMessage key={message.id} message={message} controls={<div/>} style="message" replies={replies[message.id.toString()]}/>
  )
  return(
    <div>
      <СMessage message={thread} controls={<CThreadCtrl thread={thread}/>} style="op" replies={replies[thread.id.toString()]}/>
      {messages}
      <hr className="hr"/>
    </div>
)}
