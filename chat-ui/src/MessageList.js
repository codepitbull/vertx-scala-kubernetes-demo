import React, { Component } from 'react';
import MuiThemeProvider from 'material-ui/styles/MuiThemeProvider';

import {List, ListItem} from 'material-ui/List';
import Divider from 'material-ui/Divider';

import './MessageList.css';

class MessageList extends Component {

    constructor(props) {
        super(props);
        var eb = props.eb;
        this.state = { messages: [] };

        this.setState = this.setState.bind(this);

        var that = this;

        eb.onopen = function() {
            eb.registerHandler("browser", function (error, message) {
                console.log("received " + JSON.stringify(message));
                that.setState({ messages: [...that.state.messages, message.body] });
            });
        };
    }

    renderListItems() {
        return this.state.messages.map(msg => (
            <div>
            <ListItem
                primaryText={msg.msg}
                secondaryText={msg.date}
                secondaryTextLines={1}
            />
            <Divider inset={false} />
            </div>
        ));
    }

    render() {
        return (
            <MuiThemeProvider>
                <List>
                    <Divider inset={false} />
                    {this.renderListItems()}
                </List>
            </MuiThemeProvider>
        );
    }
}

export default MessageList;