'use strict'

import Autolinker from 'autolinker';
import React from 'react';

class ReactAutolinker extends React.Component {

  constructor(props) { super(props) }

  render() {
    const {
      options,
      text,
      renderLink
    } = this.props;

    const tags = [];
    Autolinker.link(text, {options, replaceFn: (match) => {
      const tag = match.buildTag();
      tags.push(tag);
      return tag;
    }});

    let _text = text;
    const childern = [];
    for(let tag of tags) {
      const parts = _text.split(tag.attrs.href);
      if (tag.attrs && tag.attrs.class) {
        tag.attrs.className = tag.attrs.class;
        delete tag.attrs.class;
      }
      tag.attrs.key = `${tag.attrs.href}-${tags.indexOf(tag)}`
      childern.push(parts.shift());
      childern.push(renderLink(tag));
      _text = parts.join(tag.attrs.href);
    }
    const content = childern.length ? childern : text;
    return (
      <div>{content}</div>
    )
  }
}
ReactAutolinker.defaultProps = {
  options: {},
  renderLink: (tag) => React.createElement(tag.tagName, tag.attrs, tag.innerHtml)
}

export default ReactAutolinker;
