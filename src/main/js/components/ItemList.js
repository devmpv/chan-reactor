'use strict';

import ThreadPreview from "./ThreadPreview";
import Message from "./Message";

// tag::vars[]
const React = require('react');
const ReactDOM = require('react-dom');
// end::vars[]

class ItemList extends React.Component {

    constructor(props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    // tag::handle-page-size-updates[]
    handleInput(e) {
        e.preventDefault();
        let pageSize = ReactDOM.findDOMNode(this.refs.pageSize).value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.updatePageSize(pageSize);
        } else {
            ReactDOM.findDOMNode(this.refs.pageSize).value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }

    // end::handle-page-size-updates[]

    // tag::handle-nav[]
    handleNavFirst(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }

    // end::handle-nav[]

    // tag::message-list-render[]
    render() {
        let firstItem = this.props.items[0];
        let items;
        if (firstItem) {
            if (firstItem._links.board) {
                items = this.props.items.map(item =>
                    <ThreadPreview key={item._links.self.href} thread={item} onDelete={this.props.onDelete}/>
                );
            } else {
                items = this.props.items.map(item =>
                    <Message key={item._links.self.href} message={item} onDelete={this.props.onDelete}/>
                );
            }
        }

        let navLinks = [];
        if ("first" in this.props.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                {items}
                <div>
                    {navLinks}
                </div>
                <input ref="pageSize" defaultValue={this.props.pageSize} onInput={this.handleInput}/>
            </div>
        )
    }

    // end::message-list-render[]
}

export default ItemList;
