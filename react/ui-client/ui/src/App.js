import React from 'react';
import './App.css';
import Login from "./Login";
import {BrowserRouter as Router, Route, Switch} from 'react-router-dom';
import Home from "./Home";
import AppNavbar from "./AppNavbar";

function App() {
    return (
        <Router>
            <div>
                <AppNavbar/>
                <Switch>
                    <Route path='/' exact={true} component={Home}/>
                    <Route path='/login' exact={true} component={Login}/>
                </Switch>
            </div>
        </Router>
    );
}

export default App;
