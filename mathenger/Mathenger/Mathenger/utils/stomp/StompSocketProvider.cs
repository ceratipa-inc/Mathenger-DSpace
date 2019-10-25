using System;
using System.Collections.Generic;
using System.Windows;
using System.Windows.Threading;
using Mathenger.config;
using WebSocketSharp;

namespace Mathenger.utils.stomp
{
    public class StompSocketProvider
    {
        private WebSocket _socket;

        private IDictionary<string, Action<StompMessage>> _messageHandlers =
            new Dictionary<string, Action<StompMessage>>();

        private StompMessageSerializer _serializer;
        private ApplicationProperties _properties;
        private IDictionary<string, WebSocket> _sockets = new Dictionary<string, WebSocket>();

        public StompSocketProvider(StompMessageSerializer serializer, ApplicationProperties properties)
        {
            _serializer = serializer;
            _properties = properties;
            InitializeWebSocket();
        }

        public void Subscribe(string id, string destination, Action<StompMessage> messageHandler)
        {
            var sub = new StompMessage("SUBSCRIBE");
            sub["id"] = id;
            sub["destination"] = $"/topic/{destination}";
            _socket.SendAsync(_serializer.Serialize(sub), completed =>
            {
                if (completed)
                {
                    _messageHandlers.Add(id, messageHandler);
                }
                else
                {
                    Dispatcher.CurrentDispatcher.Invoke(() => MessageBox.Show("Socket subscription failed"));
                }
            });
        }

        public void SendMessage(string destination, string json, Action<bool> completed = null)
        {
            var broad = new StompMessage("SEND", json);
            broad["content-type"] = "application/json";
            broad["destination"] = destination;
            broad.Headers.Add("Authorization", _properties.AuthToken);
            _socket.SendAsync(_serializer.Serialize(broad), completed);
        }

        public WebSocket GetSocket(string subscribeDestination)
        {
            if (_sockets.ContainsKey(subscribeDestination))
            {
                return _sockets[subscribeDestination];
            }

            var ws = new WebSocket(_properties.WebSocketEndpointUrl);
            ws.OnError += (sender, args) =>
            {
                Dispatcher.CurrentDispatcher.Invoke(() => { MessageBox.Show(args.Message); });
            };

            ws.Connect();

            var connect = new StompMessage("CONNECT");
            connect["accept-version"] = "1.2";
            connect["host"] = "";
            ws.Send(_serializer.Serialize(connect));

            var sub = new StompMessage("SUBSCRIBE");
            sub["id"] = "sub-0";
            sub["destination"] = $"/topic/{subscribeDestination}";
            ws.Send(_serializer.Serialize(sub));
            _sockets.Add(subscribeDestination, ws);
            return ws;
        }

        public void Disconnect()
        {
            var disconnect = new StompMessage(StompFrame.DISCONNECT);
            _socket.Send(_serializer.Serialize(disconnect));
            _socket.Close();
        }

        private void InitializeWebSocket()
        {
            _socket = new WebSocket(_properties.WebSocketEndpointUrl);
            _socket.OnMessage += (sender, args) =>
            {
                var msg = _serializer.Deserialize(args.Data);
                if (msg.Command == StompFrame.MESSAGE)
                {
                    var id = msg.Headers["subscription"];
                    _messageHandlers[id]?.Invoke(msg);
                }
            };
            _socket.OnError += (sender, args) =>
            {
                Dispatcher.CurrentDispatcher.Invoke(() => { MessageBox.Show(args.Message); });
            };
            _socket.Connect();
            var connect = new StompMessage("CONNECT");
            connect["accept-version"] = "1.2";
            connect["host"] = "";
            connect.Headers.Add("Authorization", _properties.AuthToken);
            _socket.Send(_serializer.Serialize(connect));
        }
    }
}