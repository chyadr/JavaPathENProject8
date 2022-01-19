./build-gpsutil.sh
./build-tourguide.sh
./build-rewardcentral.sh
./build-trippricer.sh
docker build . -t gpsutil -f Dockerfile.gpsutil
docker build . -t tourguide -f Dockerfile.tourguide
docker build . -t rewardcentral -f Dockerfile.rewardcentral
docker build . -t trippricer -f Dockerfile.trippricer

docker-compose up