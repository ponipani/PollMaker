# PollMaker

A standalone server Spring Boot REST Java 8 test application for ONTravelSolutions.

=============================

REST api

Resources:

1. Poll

1.1 Creating a poll

	POST http://{host}:{port}/api/polls
			
		{
			"theme" : <string>,
			"answers" : [{ "text" : <string> }, { "text" : <string> }, ...]
		}
	
	Example
	
		POST http://localhost:8080/api/polls

			{
				"theme" : "theme 1",
				"answers" : [{ "text" : "answer 1" }, { "text" : "answer 2" }]
			}
	
		Response Header
	
			Location: http://localhost:8080/api/poll/0

1.2 Getting	poll data	

	GET http://{host}:{port}/api/poll/{id}
		
	Example
	
		GET http://localhost:8080/api/poll/0
		
		Response Body
		
			{
				"id": 0,
				"theme": "theme 1",
				"status": "CREATED",
				"answers": [
					{
						"id": 0,
			      		"text": "answer 1"
			    	},
			    	{
			      		"id": 1,
			      		"text": "answer 2"
			    	}
			  	],
			  	"links": [
			    	{
			      		"rel": "self",
			      		"href": "http://localhost:8080/api/poll/0"
			    	},
			    	{
			      		"rel": "statistic",
			      		"href": "http://localhost:8080/api/poll/0/statistic"
			    	}
			  	]
			}
			
	Notes
	
		If a poll status is "STARTED" poll links will also contatin a "vote" link:
		
			{
	      		"rel": "vote",
	      		"href": "http://localhost:8080/api/poll/{id}/votes"
	    	}

1.3 Starting a poll

	PATCH http://{host}:{port}/api/poll/{id}

		{
			status : "STARTED"
		}
		
	Example
	
		PATCH http://localhost:8080/api/poll/0

		{
			status : "STARTED"
		}
		
		Response Body
	
			{
				"id": 0,
				"theme": "theme 1",
				"status": "STARTED",
				"answers": [
					{
						"id": 0,
				      	"text": "answer 1"
				   	},
				   	{
				   		"id": 1,
				      	"text": "answer 2"
				   	}
				],
				"links": [
					{
				   		"rel": "self",
				      	"href": "http://localhost:8080/api/poll/0"
				   	},
				   	{
				   		"rel": "statistic",
				   		"href": "http://localhost:8080/api/poll/0/statistic"
				   	},
				   	{
				   		"rel": "vote",
				   		"href": "http://localhost:8080/api/poll/0/votes"
				   	}
				 ]
			}
		
	Notes
	
		Permitted only for polls with status "CREATED"
	
1.4 Submiting a vote

	POST http://{host}:{port}/api/poll/{id}/votes
	
		{
			"answerId" : <int>
		}
		
	Example
	
		POST http://localhost:8080/api/poll/0/votes
	
		{
			"answerId" : 0
		}
		
		Response Header
	
			Location: http://localhost:8080/api/poll/0
			
	Notes
	
		Permitted only for polls with status "STARTED"

1.5 Closing a poll

	PATCH http://{host}:{port}/api/poll/{id}

		{
			status : "STARTED"
		}
		
	Example
	
		PATCH http://localhost:8080/api/poll/0

		{
			status : "CLOSED"
		}
		
		Response Body
	
			{
				"id": 0,
				"theme": "theme 1",
				"status": "CLOSED",
				"answers": [
					{
						"id": 0,
				      	"text": "answer 1"
				   	},
				   	{
				   		"id": 1,
				      	"text": "answer 2"
				   	}
				],
				"links": [
					{
				   		"rel": "self",
				      	"href": "http://localhost:8080/api/poll/0"
				   	},
				   	{
				   		"rel": "statistic",
				   		"href": "http://localhost:8080/api/poll/0/statistic"
				   	}
				 ]
			}
		
	Notes
	
		Permitted only for polls with status "STARTED"
		
1.6 Getting poll statistic

	GET http://{host}:{port}/api/poll/{id}/statistic
	
	Example
	
		GET http://localhost:8080/api/poll/0/statistic
		
		Response Body
		
			[
				"answer 1 - 1",
				"answer 2 - 0"
			]
			
		Response Header
	
			Location: http://localhost:8080/api/poll/0
