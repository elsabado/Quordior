# Quoridor

## Description
This project is a remake of the multiplayer board game Quoridor. It can be played as either 2 or 4 players, for details on how to play see: https://en.wikipedia.org/wiki/Quoridor. This project has a frontend made for Android and a server backend written for SpringBoot.

## Visuals
**add later**

## Support
**add later?**

## Roadmap
- Unit 1: get familiar with tools
- Unit 2: 
    - **Frontend**
        - PHASE-1 (use jsontest.com)
            1. Check the jsontest.com site. See the different requests.
            2. Think of ONE screen which takes in input and displays some results.
            3. Play with volley/retrofit to connect to jsontest.com
            4. Connect your screen code to volley/retrofit to connect to jsontest.com and display results.
            5. Debug and fix and re-check as needed.

        - PHASE-2 (play with Postman)
            1. Learn to run postman as a server.
            2. Make sure you set up examples on postman and are able to run them on the mock server.
            3. Now - connect your Android code to use the mock server and test different calls to see that they work.
            4. Debug and fix and re-check as needed.

        - PHASE-3 (play more with Postman - this time customizing examples to YOUR PROJECT)
            1. Set up examples in your collection for YOUR project endpoints.
            2. Make sure you are able to access and demonstrate these endpoints using your AS code.

        - PHASE-4 (use localhost using H2 db)
            *Note that this will let you test that your code can work with your project (with H2DB)*
            3. Download, build, run your team's server code but using H2 db on LOCALHOST
            4. Connect your AS code to your team's server and check to see it works.
            5. Debug and fix and re-check as needed.

        - PHASE-5 (use remote host)
            *Note that this will let you test that your code can work with your project with remote server*
            1. Connect your AS code to your team's remote server and check to see it works.
            2. Debug and fix and re-check as needed.
    - **Backend**
        - PHASE-1 (localhost using H2)
            1. Create Springboot for Controllers and Entities. Just one table is enough for the MINIMAL roundtrip.
            2. Use H2 DB  and H2-Console
            3. Test locally using POSTMAN
            4. Debug and fix and re-check as needed.

        - PHASE-2 (localhost using mysql)
            1. Now, get a local MySQL DB working
            2. Also set up MySQLWorkbench
            3. Connect your server to the local mysql DB.
            4. Test locally using POSTMAN and MySqlWorkbench
            5. Debug and fix and re-check as needed.
        - PHASE-3 (remote DB)
            1. SSH to remote server
            2. Install and setup the remote DB. 
            3. Test using MySQLWorkbench.
            4. Connect your LOCAL server springboot code to the remote DB.
            5. Test locally using POSTMAN and MySQLWorkbench
            6. Debug and fix and re-check as needed.
        - PHASE-4 (remote server upload)
            1. SSH to remote server. Copy your jar file over to the server (using scp or filezilla).
            2. Run your server using java -jar <JARFILENAME>  will run it. Assuming that port 8080 is open on your server.
            3. Test your remote server using POSTMAN as client
            4. Debug and fix and re-check as needed.
        - PHASE-5 (Android Client)
            1. Work with your Android team-members to make sure that the Android code and the Springboot code work together.
            *Now that you have the basic round-trip working, work on core features of your project.*
        
    Add tables and screens as needed and add functionality as per your project to work beyond “minimal round-trip”.
    - Other
        - Use issues and merge-request feature in GitLab
            - Create a milestone.
            - Create labels for issues, or choose the default set which includes: bug, confirmed, critical, discussion, documentation enhancement, suggestion, support.
            - Create issues for known bugs in the app, new features/enhancements, needed documentation, etc. Assign the issue to the milestone with the appropriate label.

- Unit 3:
    The main goal for teams is to:
    (1) have the application's core features working (as best as feasible). This includes implementing 1-1, 1-many, many-many relationships.

    (2) Implement CI/CD

    (3) continue using git issues and merge-requests + do code reviews.

- Unit n: Game is Finished

## Team Members
- Carter Awbrey
- Ellery Sabado
- Mazin Bashier
- Seth Leon

## License
GNU GPL

## Project status
In active development by core team members as of 2/2/2023.
