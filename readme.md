# R3almx - Java Edition

welcome to r3almx re-imagine. This repo is a re-write of r3almx backend going from python's FastAPI to java's SpringBoot.

# Reasons

### language limitations

<h3>Async</h3>python's GIL limits python's too good to be true nature. Java offers real concurrency whereas python switches from task to task in a serial manner. Since r3almx is primarily a real-time app, we cant rely on python to handle tasks and websockets and expect our instances to scale well horizontally.
<h3>Strongly Typed</h3>Too many times i've ran into debugging issues caused by weakly typed nature of python. Although we can use comprehensions and other functional methods and produce a very nice solution for data aggregation and response building etc, it ends up costing mis-firing of events and unreliable response times. Java finds a nice balance between strongly typed and still providing a decent amount of generics control.  
<h3>Enforced Patterns</h3>There's countless patterns in java as its an object oriented language. Patterns such as the Bean pattern and Repository-Service patterns are extremely mature and as a backend developer who's been writing backends/RESTful APIs in python, the enforced patterns extremely easy to pick up. Throughout my 4 years of experience writing Backend code and logic, i've matured as a developer and produced many different backend system designs and towards the end of my python crusade and exploring other frameworks, I had realized that I was naturally started implementing SpringBoot's Bean and Repository-Service pattern in my python backends, quite sloppily of course, however after writing some SpringBoot APIs, patterns started emerging and picking up SpringBoot was extremely easy and natural. Something I designed organically in my freeform backend designs, was something that was a standard in another framework. 
<h3>SpringBoot as a framework</h3>SpringBoot is extremely matured and is used world-wide and is the first choice for enterprise backend implementation whereas FastAPI is not recognised anywhere. Although using popularity as a choice maker is quite shallow, maturity comes with an ecosystem. Python's FastAPI doesn't have an ecosystem however python offer's an extremely vast ecosystem, with the likes of SQLAlchemy which is a tremendous ORM and is extremely powerful. However, Java offers the same features along with java's speed, performance and security.

# List of implementations

## Auth

- [ ] Register
  - [x] Email and Password
  - [ ] OAuth through google
- [x] Login
  - [x] Email and Password
  - [x] JWT generation
  - [ ] Google's OAuth

## Chat Room

- [x] Websocket
  - [x] Authenticate
  - [x] Receive
  - [x] Broadcast
- [ ] Room Manager
  - [ ] Init RabbitMQ Queue
    - [ ] rooms Dict[str, set]
    - [ ] rabbit_queues Dict[str, rabbitmq_queue]
    - [ ] rabbit_channels Dict[str, rabbitmq_channel]
    - [ ] broadcast_tasks Dict[str, tasks]
  - [x] Connect User
  - [x] Disconnect User
  - [ ] Add message to Queue
  - [ ] Add message to cache
  - [ ] Populate Cache
  - [ ] Start Broadcast Task
  - [ ] Stop Broadcast Task
- [ ] Digestion Broker
  - [ ] Delete Message
  - [ ] Parse Timestamp
  - [ ] Add Message
  - [ ] Flush To DB
  - [ ] Start Flush Schedular

## Rooms

- [ ] Room
  - [x] Create
  - [x] Read
  - [ ] Edit
  - [ ] Delete
  - [x] generate invite key
- [ ] Invites
  - [x] Fetch
  - [ ] Update Invites
  - [x] Join Room
- [ ] Channels
  - [ ] Create
  - [ ] Read
  - [ ] Edit
  - [ ] Delete
- [ ] Messages
  - [ ] Create
  - [ ] Read
  - [ ] Delete
