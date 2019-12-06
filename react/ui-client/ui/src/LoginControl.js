import React, {Component} from "react";
import NavLink from "react-bootstrap/NavLink";
import NavItem from "react-bootstrap/NavItem";
import {withRouter} from "react-router";
import AuthService from "./AuthService";

class LoginControl extends Component {

    constructor(props) {
        super(props);
        this.handleLogout = this.handleLogout.bind(this);
    }

    handleLogout() {
        localStorage.removeItem("authToken");
        this.props.history.push('/');
    }

    render() {
        return (
            <NavItem>
                {
                    AuthService.isAuthenticated() ? this.Logout() : this.Login()

                }
            </NavItem>
        )
    }

    Logout() {
        return <NavLink onClick={this.handleLogout}>Logout</NavLink>;
    }

    Login() {
        return <NavLink href="/login">Login</NavLink>;
    }
}

export default withRouter(LoginControl);