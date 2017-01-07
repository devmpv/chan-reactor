'use strict';

// tag::vars[]
const React = require('react');
const block = {
        display: 'block'
      };
const none = {
        display: 'none'
      };
// end::vars[]

// tag::message[]
class ContentViewer extends React.Component {
    handleThumbClick: (event: SyntheticInputEvent)=>void;
    constructor(props) {
        super(props);
        this.handleThumbClick = (event: SyntheticInputEvent) => {
            this.props.onThumbClick('');
        }
    }

    render() {
        let style = this.props.content.visible ? block : none;
        return (
            <div style={style} id="content-viewer">
                <img id='content' src={this.props.content.src} onClick={this.handleThumbClick}/>
            </div>
        )
    }
}
// end::message[]

ContentViewer.propTypes = {
  content: React.PropTypes.object.isRequired,
  onThumbClick: React.PropTypes.func.isRequired
}

export default ContentViewer;
