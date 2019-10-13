%Implementation of MLP linear
clc
clear all

%read in data
data=xlsread('data_data.xlsx');

[m,n]=size(data);
for i=1:n
    data(:,i)=(data(:,i)-mean(data(:,i)))/sqrt(var(data(:,i)));
end
mu=10^-2; %learning rate

data1=data(:,1:n-1);
epo=0;                  %epoch
eps=0.0001;
b=1;
W1_1=ones(2,1)*eps;
b_1_1=eps;              %bias hidden neuron 1
W1_2=ones(2,1)*eps;     %weight (input1, hidden1)
b_1_2=eps;              %bias hidden neuron 2
W2_1=ones(2,1)*eps;     %weight (input2, hidden1)
b_2=eps;                %bias hidden neuron 2


while true
    epo=epo+1;
    del_2_1=zeros(m,1);
    del_1_1=zeros(m,1);
    del_1_2=zeros(m,1);

    for i=1:m
        %non linear function. z=sum(data*weight)+b
        %Input -> Hidden layer
        Z1_1(i,1)=b_1_1+data1(i,:)*W1_1;
        Z1_1(i,2)=b_1_2+data1(i,:)*W1_2;
        
        %Hidden layer -> Output 
        Z2_1(i)=Z1_1(i,:)*W2_1+b_2;
        
        %calculate back propagation error
        del_2_1(i)=(Z2_1(i)-data(i,n));     %diff_out = diff(observed out, real out)
        del_1_1(i)=del_2_1(i)*W2_1(1,1);    %diff_out * W2_1(1)
        del_1_2(i)=del_2_1(i)*W2_1(2,1);    %diff_out * W2_1(2)

    end
    del_W1_1=zeros(2,1);
    del_W1_2=zeros(2,1);
    del_W2_1=zeros(2,1);
    del_b_1_1=0;
    del_b_1_2=0;
    del_b_2_1=0;
    for i=1:m
        del_W1_1=del_W1_1-mu*del_1_1(i)*data1(i,:)';    %diff W1_1
        del_W1_2=del_W1_2-mu*del_1_2(i)*data1(i,:)';    %diff W1_2
        del_W2_1=del_W2_1-mu*del_2_1(i)*Z1_1(i,:)';     %diff W2_1
        del_b_1_1=del_b_1_1-mu*del_1_1(i);
        del_b_1_2=del_b_1_2-mu*del_1_2(i);
        del_b_2_1=del_b_2_1-mu*del_2_1(i);
    end

    W1_1=W1_1+del_W1_1/m;
    W1_2=W1_2+del_W1_2/m;
    W2_1=W2_1+del_W2_1/m;
    b_1_1=b_1_1+del_b_1_1/m;
    b_1_2=b_1_2+del_b_1_2/m;
    b_2=b_2+del_b_2_1/m;

    per(epo,1)=sqrt(dot((Z2_1'-data(:,n)),(Z2_1'-data(:,n))));
    per(epo,2)=epo;
    if norm(Z2_1-data(:,n)')/norm(data(:,n))<10^-3 || epo>=2040
        Z2_1'
        epo
        break;
    end
end
[a,b]=min(per)
% plot(per(:,2),per(:,1))
hold on
scatter3(data(:,1),data(:,2),data(:,3))
scatter3(data(:,1),data(:,2),Z2_1)
hold off









