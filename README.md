# Mathenger with DSpace integration

# How to run Mathenger with DSpace integration via docker-compose

### Add the following alias to ~/.bash_profile for convenience<br>

`alias docker-compose-mds='docker-compose -p mathenger-dspace -f dspace-angular/docker/docker-compose.yml -f dspace-angular/docker/docker-compose-rest.yml -f dspace-angular/docker/docker-compose-mathenger.yml’`

### Run `docker-compose-mds up`<br>


# Useful commands (while docker-compose is running)
### Add dummy data<br>
`docker-compose -p mathenger-dspace -f dspace-angular/docker/cli.yml -f dspace-angular/docker/cli.ingest.yml run --rm dspace-cli`
### Add admin user `test@test.edu` with password `admin` <br>
`docker-compose -p mathenger-dspace -f dspace-angular/docker/cli.yml run --rm dspace-cli create-administrator -e test@test.edu -f admin -l user -p admin -c en`
