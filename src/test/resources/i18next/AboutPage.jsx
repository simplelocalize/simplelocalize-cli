import React from "react";
import {Col, Grid, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import Header from "../components/Header";
import {FormattedMessage} from "react-intl";

const AboutPage = () => {

  function getText(value: string) {
    const myMessage = t("findWithSpace");
    const myMessage = t("findWithAssignment");
    //this function should not be found by i18next extractor
  }

  return (
    <div>
      {getText("Test")}
      <Header
        photo={"//images.placeflare.com/static/header_optimized.jpg"}
        height={280}
      >
        <h1 className="header-title"><FontAwesomeIcon icon="hand-spock"/>&nbsp;
          About us
        </h1>
      </Header>
      <Grid>
        <Col md={12}>
          <div className="text-justify" style={{lineHeight: "1.6em"}}>
            <Row>
              <h2>{t("Welcome to React")}</h2>

              <div>{t("simpleContentJSX")}</div>
              <Trans i18nKey='userMessagesUnreadJSX' count={count}>
                Hello <strong title={t('nameTitleJSX')}>{{name}}</strong>, you have {{count}} unread message. <Link
                to="/msgs">Go to messages</Link>.
              </Trans>
              <h2><strong>More about PlaceFlare?</strong></h2>
              <Col md={3}>
                <img
                  style={{paddingTop: "14px"}}
                  src="//images.placeflare.com/static/main_logo.png"
                  alt="logo placeflare"
                  className="img-responsive center-block"
                />
              </Col>
              <Col md={9}>
                <p>
                  PlaceFlare is the place where we discover and gather the most
                  beautiful places in Poland and in the
                  world. <strong>You will not find here obvious tourist
                  attractions like the Palace of Culture and Science
                  in
                  Warsaw, but you will discover many of the treasures of your
                  immediate area, about which you had no
                  idea.</strong>

                </p>
                <p>The idea of a web and mobile application, which would be a kind
                  of list of interesting places in
                  Poland, was created more than two years ago. We are constantly
                  working to expand our base - the places
                  are manually selected, described and shared with users. We focus
                  on quality over quantity, so you will
                  not find here obvious points of interest such as Palace of
                  Culture and Science or Wroclaw Zoo, because
                  these places know everyone, they are certainly better described
                  in urban guides, and that is why we
                  focus on the treasures of the area. The current project, made
                  available in April this year, lists places
                  from all over the Poland, as well as from the Czech Republic and
                  the United Kingdom.
                </p>
              </Col>
            </Row>
            <Row style={{
              paddingTop: "30px",
              paddingBottom: "30px"
            }}>
              <Col md={12}>
                <Col md={3}>
                  <img
                    src="//images.placeflare.com/static/about/kubele-aparat.jpeg"
                    alt="map"
                    className="img-responsive"
                    style={{borderRadius: "6px"}}
                  />
                </Col>

                <Col md={3}>
                  <img
                    src="//images.placeflare.com/static/about/kingele-kawa.jpeg"
                    alt="map"
                    className="img-responsive"
                    style={{borderRadius: "6px"}}
                  />
                </Col>

                <Col md={3}>
                  <img
                    src="//images.placeflare.com/static/about/kubele-ucieka.jpeg"
                    alt="map"
                    className="img-responsive"
                    style={{borderRadius: "6px"}}
                  />
                </Col>
                <Col md={3}>
                  <img
                    src="//images.placeflare.com/static/about/kubele-fotograf.jpg"
                    alt="map"
                    className="img-responsive"
                    style={{borderRadius: "6px"}}
                  />
                </Col>
              </Col>
            </Row>

            <Row>
              <Col md={12}>
                <p>
                  We want to encourage users to visit a really interesting and
                  beautiful places that are closest to the
                  specified location. Very often, it turns out that we do not know
                  and appreciate what we have just under
                  the nose. With PlaceFlare we want to make it easier for you to
                  choose a weekend trip or one day trip.
                  Get on a bike, in a car or train and let us show you how
                  beautiful your surroundings can be!
                </p>
              </Col>
            </Row>

            <Row className="m-2" style={{paddingBottom: "20px"}}>
              <h2><strong><FormattedMessage id="DONATION"/></strong></h2>
              <Col md={12}>
                <p>
                  Would you like to support us? Write to us we really like to hear
                  supportive comments on <a
                  href="mailto:contact@placeflare.com">contact@placeflare.com</a>,
                  and if you REALLY liked our work on
                  this project you can make a donation using PayPal. <a
                  href="https://www.paypal.me/jakubpomykala">https://www.paypal.me/jakubpomykala</a>.
                  Thanks!
                </p>
              </Col>
            </Row>

            <Row className="m-2" style={{paddingBottom: "20px"}}>
              <h2><strong>
                <FormattedMessage id="FURTHER_WORK"/>
              </strong></h2>
              <Col md={12}>
                <p>
                  We want to encourage users to visit a really interesting and
                  beautiful places that are closest to the
                  specified location. Very often, it turns out that we do not know
                  and appreciate what we have just under
                  the nose. With PlaceFlare we want to make it easier for you to
                  choose a weekend trip or one day trip.
                  Get on a bike, in a car or train and let us show you how
                  beautiful your surroundings can be!
                </p>
              </Col>
            </Row>
          </div>
        </Col>
      </Grid>
    </div>);
};
export default AboutPage;
