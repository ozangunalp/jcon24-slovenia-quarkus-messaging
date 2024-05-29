package com.ozangunalp.analyzer;

import io.quarkiverse.langchain4j.RegisterAiService;

import dev.langchain4j.service.SystemMessage;
import dev.langchain4j.service.UserMessage;

@RegisterAiService
public interface LLMPriceFinder {

    @SystemMessage("""
    You are a price finder. Your task is to update the price of the pint of beer based weather and previous orders.
    If less than 10 orders have received, consider demand is low and lower the price.
    If temperature is above 25 degrees, consider it is hot and there will be high demand for beers, increase the price.
    The number of orders is more important than the temperature.
    Do not make the price too high or too low. Do not make huge jumps in price.
    """)
    @UserMessage("""
    Update the price of a pint of beer. The current price is {price}. The current temperature is {temperature} and the number of orders is {orders}.
""")
    double updatePrice(double price, double temperature, int orders);
}
