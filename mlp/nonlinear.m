function [ y ] = nonlinear( z )
%NONLINEAR function
%   To convert the 1st hidden layer into nonlinear neurons
%   via y = 1/(1+exp(-z))
    [m,n]=size(z);
    
    for i=1:n
        y(m,i) = 1/(1+exp(-z(m,i)));
    end
end

