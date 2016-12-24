'use strict';

import MainPage from "./components/MainPage";
import BoardView from "./components/BoardView";
import ThreadView from "./components/ThreadView";

const React = require('react');
const ReactDOM = require('react-dom');
const Router = require('react-router').Router;
const Route = require('react-router').Route;
const browserHistory = require('react-router').browserHistory;

ReactDOM.render(
    <Router history={browserHistory}>
        <Route path="/:boardName" component={BoardView}/>
        <Route path="/:boardName/thread/:threadId" component={ThreadView}/>
        <Route path="/:boardName/*" component={BoardView}/>
        <Route path="/" component={MainPage}/>
    </Router>,
    document.getElementById('react')
);
