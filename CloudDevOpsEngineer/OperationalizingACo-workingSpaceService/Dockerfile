FROM python:3.11.5-bookworm
WORKDIR /app
COPY ./analytics/requirements.txt /app/requirements.txt
RUN pip install --no-cache-dir -r requirements.txt
COPY ./analytics /app/.
EXPOSE 5153
CMD python app.py