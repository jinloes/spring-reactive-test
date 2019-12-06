import React, {Component} from "react";
import {Button} from "react-bootstrap";
import "./Login.css";
import {withRouter} from 'react-router-dom'
import Form from "react-bootstrap/Form";
import Alert from "react-bootstrap/Alert";

class Login extends Component {
    constructor(props) {
        super(props);
        this.state = {
            username: '',
            password: '',
            formErrors: '',
            validated: false,
        };
        this.handleSubmit = this.handleSubmit.bind(this);
    }

    async handleSubmit(event) {
        event.preventDefault();

        const form = event.currentTarget;
        this.setState({validated: true, formErrors: ''});
        if (form.checkValidity() === false) {
            event.stopPropagation();
            return;
        }

        const clientId = process.env.REACT_APP_API_KEY;
        const clientSecret = process.env.REACT_APP_API_SECRET;
        const authCreds = new Buffer(clientId + ":" + clientSecret).toString('base64');
        const formData = new URLSearchParams();

        formData.append('grant_type', 'password');
        formData.append('username', this.state.username);
        formData.append('password', this.state.password);
        formData.append('scope', 'user_info');

        let resp = await fetch('/oauth/token', {
            withCredentials: true,
            credentials: 'include',
            method: 'POST',
            headers: {
                'Authorization': 'Basic ' + authCreds,
                'Accept': 'application/json',
                'Content-Type': 'application/x-www-form-urlencoded'
            },
            body: formData.toString(),
        });

        if (resp.ok) {
            let body = await resp.json();
            localStorage.setItem("authToken", body['access_token']);
            this.props.history.push('/');
        } else {
            let body = await resp.json();
            console.log(body);
            if (body.error === 'invalid_grant') {
                this.setState({formErrors: "Invalid username or password."});
            }
        }
    }

    render() {
        return (
            <div className="Login">
                <Form noValidate validated={this.state.validated} onSubmit={this.handleSubmit}>
                    {this.state.formErrors ?
                        <Alert variant='danger'>
                            {this.state.formErrors}
                        </Alert>
                        : null}
                    <Form.Group controlId="username">
                        <Form.Label>Username</Form.Label>
                        <Form.Control autoFocus
                                      type="text"
                                      size="lg"
                                      value={this.state.username}
                                      onChange={e => this.setState({username: e.target.value})}
                                      required
                        />
                        <Form.Control.Feedback type="invalid">
                            Please enter a username.
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Form.Group controlId="password">
                        <Form.Label>Password</Form.Label>
                        <Form.Control
                            value={this.state.password}
                            onChange={e => this.setState({password: e.target.value})}
                            type="password"
                            size="lg"
                            required
                        />
                        <Form.Control.Feedback type="invalid">
                            Please enter a password.
                        </Form.Control.Feedback>
                    </Form.Group>
                    <Button size="lg" type="submit" block>
                        Login
                    </Button>
                </Form>
            </div>
        );
    }
}

export default withRouter(Login);