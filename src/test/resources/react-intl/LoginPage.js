import React, {Component} from "react";
import {Col, Grid} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {FormattedMessage} from "react-intl";
import Header from "../components/Header";
import SocialLogin from "../containers/SocialLogin";

class LoginPage extends Component {

  onLoginSuccess = () => this.props.history.push("/user");

  render() {
    return (
      <div>
        <Header photo={"//images.placeflare.com/random/cover_image/3.jpg"}>
          <h1 className="header-title"><FontAwesomeIcon icon="user-plus"/>&nbsp;
            <FormattedMessage
              id="NAVBAR.JOIN_US"
              defaultMessage="Join Us!"
            />
          </h1>
        </Header>
        <Grid>
          <Col md={5}>
            <SocialLogin
              onLoginSuccess={this.onLoginSuccess}
            />

          </Col>
          <Col md={7}>
            <h2 className="text-center">
              Sign up will take you only few seconds!
            </h2>
          </Col>
        </Grid>
      </div>
    );
  }
}

export default LoginPage;
