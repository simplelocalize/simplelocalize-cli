import React, {Component} from "react";
import {Col, Grid} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {FormattedMessage} from "react-intl";
import Header from "../components/Header";
import SocialLogin from "../containers/SocialLogin";

class LoginPage extends Component {

  render() {
    return (
      <header>
        <div className="container">
          <div className="row">
            <div className="col-md-7 col-12">
              <div className="header-content">
                <h1>
                  <FormattedHTMLMessage id="header-text"/>
                </h1>
                <h3>
                  <FormattedHTMLMessage id="header-subtitle"/>
                </h3>
                <LinkTranslated to="/offer" className="btn btn-primary btn-cta">
                  <FormattedMessage id="header-button"/>
                </LinkTranslated>
              </div>
            </div>
            <div className="col-md-5 d-md-block d-none">
              <img src={machine} className="img-fluid px-5 slide-in-bottom" alt="vendingmetrics vending machine"/>
            </div>
          </div>
        </div>
      </header>
    );
  }
}

export default LoginPage;

