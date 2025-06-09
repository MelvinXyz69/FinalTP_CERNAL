from django.db import models

class PortfolioItem(models.Model):
    title = models.CharField(max_length=200)
    description = models.TextField()
    image = models.ImageField(upload_to='portfolio_images/', blank=True, null=True)  # Optional image
    url = models.URLField(blank=True, null=True)  # Optional URL