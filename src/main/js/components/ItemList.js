// @flow

'use strict';

import ThreadPreview from "./ThreadPreview";
import Message from "./Message";

const React = require('react');

type Props = {params: {}}

class ItemList extends React.Component {
    handleNavFirst: Function;
    handleNavPrev: Function;
    handleNavNext: Function;
    handleNavLast: Function;
    handleInput: Function;

    constructor(props: Props) {
        super(props);
        this.handleNavFirst = this.handleNavFirst.bind(this);
        this.handleNavPrev = this.handleNavPrev.bind(this);
        this.handleNavNext = this.handleNavNext.bind(this);
        this.handleNavLast = this.handleNavLast.bind(this);
        this.handleInput = this.handleInput.bind(this);
    }

    // tag::handle-page-size-updates[]
    handleInput(e: SyntheticInputEvent) {
        e.preventDefault();
        let pageSize = this.refs.pageSize.value;
        if (/^[0-9]+$/.test(pageSize)) {
            this.props.params.updatePageSize(pageSize);
        } else {
            this.refs.pageSize.value =
                pageSize.substring(0, pageSize.length - 1);
        }
    }

    // end::handle-page-size-updates[]

    // tag::handle-nav[]
    handleNavFirst(e: SyntheticInputEvent) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.first.href);
    }

    handleNavPrev(e: SyntheticInputEvent) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.prev.href);
    }

    handleNavNext(e: SyntheticInputEvent) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.next.href);
    }

    handleNavLast(e: SyntheticInputEvent) {
        e.preventDefault();
        this.props.onNavigate(this.props.links.last.href);
    }

    // end::handle-nav[]

    // tag::message-list-render[]
    render() {
        let items;
        if (this.props.params.board) {
            items = this.props.params.items.map(item =>
                <ThreadPreview key={item._links.self.href} thread={item}
                               onThumbClick={this.props.params.onThumbClick}
                               onDelete={this.props.params.onDelete}
                               onDialogOpen={this.props.params.onDialogOpen}/>
            );
        } else {
            items = this.props.params.items.map(item =>
                <Message key={item._links.self.href} message={item}
                         board={this.props.params.board}
                         onThumbClick={this.props.params.onThumbClick}
                         onDelete={this.props.params.onDelete}
                         onDialogOpen={this.props.params.onDialogOpen}/>
            );
        }


        let navLinks = [];
        if ("first" in this.props.params.links) {
            navLinks.push(<button key="first" onClick={this.handleNavFirst}>&lt;&lt;</button>);
        }
        if ("prev" in this.props.params.links) {
            navLinks.push(<button key="prev" onClick={this.handleNavPrev}>&lt;</button>);
        }
        if ("next" in this.props.params.links) {
            navLinks.push(<button key="next" onClick={this.handleNavNext}>&gt;</button>);
        }
        if ("last" in this.props.params.links) {
            navLinks.push(<button key="last" onClick={this.handleNavLast}>&gt;&gt;</button>);
        }

        return (
            <div>
                {items}
                <div>
                    {navLinks}
                </div>
                <input ref="pageSize" defaultValue={this.props.params.pageSize} onInput={this.handleInput}/>
            </div>
        )
    }
}

export default ItemList;
