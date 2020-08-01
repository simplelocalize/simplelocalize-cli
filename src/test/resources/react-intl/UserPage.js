import React, {Component} from "react";
import {Col, Grid, Panel, Row} from "react-bootstrap";
import {FontAwesomeIcon} from "@fortawesome/react-fontawesome";
import {PulseLoader} from "halogenium";
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import {defineMessages, FormattedMessage, injectIntl} from "react-intl";
import Axios from "../api";
import Header from "../components/Header";
import PublicUserPanel from "../containers/PublicUserPanel";
import SignedUserPanel from "../containers/SignedUserPanel";
import ActivityView from "../components/user/ActivityView";
import ImageUploaderGroup from "../components/images/ImageUploaderGroup";
import UserInfoPanel from "../components/user/UserInfoPanel";
import {TOKEN_KEY} from "../api/storage";
import {logoutUser} from "../redux/actions/userActions";

class UserPage extends Component {

  constructor(props) {
    super(props);
    let userId = this.props.match.params.id;
    let showingLoggedUser = false;
    if (!userId) {
      showingLoggedUser = true;
      userId = 0;
    }
    this.state = {
      user: {},
      userId,
      activities: [],
      showingLoggedUser,
      editMode: false,
      userIsLoading: true,
      activitesAreLoading: true
    };
  }

  componentDidMount() {
    Axios.get(`/users/${this.state.userId}`).then(({data}) => this.setState({
      user: data.data,
      userIsLoading: false
    }));
    this.loadMoreActivities();
  }

  onLogoutClick = () => {
    localStorage.removeItem(TOKEN_KEY);
    this.props.logoutUser();
    this.props.history.push("/home");
  };

  setEditMode = () => this.setState({editMode: true});

  loadMoreActivities = () => {
    const {userId} = this.state;
    Axios.get(`/users/${userId}/feed`).then(({data}) => this.setState({
      activities: data.data,
      activitesAreLoading: false
    }));
  };

  onCoverUploaded = (fileResponse: any) => {
    const userUpdateForm = {
      profileCoverImage: fileResponse.data.url
    };
    Axios.put("/users", userUpdateForm).then(({data}) => this.setState({user: data.data}));
  };

  render() {
    const {user, activities, showingLoggedUser, userIsLoading, activitesAreLoading} = this.state;
    const {intl} = this.props;

    if (!user) {
      return null;
    }

    return (
      <div>
        <Header photo={user.profileCoverImage}>

          {userIsLoading ?
            <h1 className="text-center"><PulseLoader color="#e54c3c" size="22px"
                                                     margin="4px"/></h1>
            :
            <div>
              <h2 className="header-title">
                <em>&quot;{user.motto}&quot;</em>
              </h2>
              <p className="header-title">~{user.mottoAuthor}</p>
            </div>}
        </Header>

        <Grid>
          <Col md={3}>
            <Panel>
              <div className="panel-body">{userIsLoading ?
                <h1 className="text-center"><PulseLoader color="#e54c3c"
                                                         size="22px"
                                                         margin="4px"/></h1>
                :
                <div>
                  <UserInfoPanel data={user}/>
                  {showingLoggedUser
                    ?
                    <SignedUserPanel
                      onEditClick={this.setEditMode}
                      onLogoutClick={this.onLogoutClick}
                    />
                    :
                    <PublicUserPanel/>}
                </div>
              }</div>
            </Panel>
          </Col>
          <Col md={6}>
            <Row>
              <Panel>
                <div className="panel-body">{userIsLoading ?
                  <h1 className="text-center"><PulseLoader color="#e54c3c"
                                                           size="22px"
                                                           margin="4px"/>
                  </h1> :
                  <div>
                    <Col md={3} xs={6} className="text-center">
                      <h2><FontAwesomeIcon
                        icon="plus"/>&nbsp;{user.createdCount}</h2>
                      <h5 className="text-muted">
                        <FormattedMessage
                          id="CREATED_COUNT"
                          defaultMessage="Created"
                        />
                      </h5>
                    </Col>
                    <Col md={3} xs={6} className="text-center">
                      <h2><FontAwesomeIcon
                        icon="plane"/>&nbsp;{user.visitsCount}</h2>
                      <h5 className="text-muted">
                        <FormattedMessage
                          id="VISITS"
                          defaultMessage="Visits"
                        />
                      </h5>
                    </Col>
                    <Col md={3} xs={6} className="text-center">
                      <h2><FontAwesomeIcon
                        icon={"heart"}/>&nbsp;{user.likesCount}</h2>
                      <h5 className="text-muted">
                        <FormattedMessage
                          id="LIKES"
                          defaultMessage="Likes"
                        />
                      </h5>
                    </Col>
                    <Col md={3} xs={6} className="text-center">
                      <h2><FontAwesomeIcon
                        icon="comments"/>&nbsp;{user.commentsCount}</h2>
                      <h5 className="text-muted">
                        <FormattedMessage
                          id="COMMENTS"
                          defaultMessage="Comments"
                        />
                      </h5>
                    </Col>
                  </div>
                }</div>
              </Panel>
            </Row>

            <Row>
              {activitesAreLoading ?
                <h1 className="text-center"><PulseLoader color="#e54c3c"
                                                         size="22px"
                                                         margin="4px"/></h1>
                :
                <Col md={12}>
                  {activities.map(act => <ActivityView
                    data={act} key={act.id}
                    firstName={user.firstName}
                  />)}
                </Col>
              }
            </Row>
          </Col>

          <Col md={3} className="text-center">
            {showingLoggedUser ?
              <ImageUploaderGroup
                onImageUploaded={this.onCoverUploaded}
                title={intl.formatMessage(defineMessages({
                  id: 'COVER_IMAGE.TITLE',
                  defaultMessage: "Change cover photo"
                }))}
                message={intl.formatMessage({ id: "COVER_IMAGE.PLACEHOLDER",
                  defaultMessage: "Drop photo here, we will take care of everything"
                })}
              /> :
              null}

            <h2 className="text-center">
              <FormattedMessage
                id="ACHIEVEMENTS"
                defaultMessage="Achievements"
              />
            </h2>
            <h4 className="text-muted text-center">
              <FormattedMessage
                id="AVAILABLE_SOON"
                defaultMessage="Available soon!"
              />
            </h4>
          </Col>


        </Grid>
      </div>
    );
  }
}

const mapStateToProps = state => ({
  isSignedIn: state.userReducer.signedIn
});

const mapDispatchToProps = dispatch => (bindActionCreators({
  logoutUser
}, dispatch));

export default injectIntl(connect(mapStateToProps, mapDispatchToProps)(UserPage));
