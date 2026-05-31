from fastapi import FastAPI
from health import router as health_router
from status import router as status_router

app = FastAPI()

app.include_router(health_router)
app.include_router(status_router)