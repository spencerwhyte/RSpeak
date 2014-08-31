//
//  Cloud.m
//  IOSAnonQuestion
//
//  Created by Spencer Whyte on 2014-03-15.
//  Copyright (c) 2014 Spencer Whyte. All rights reserved.
//

#import "Cloud.h"


static Cloud * sharedInstance = nil;

@implementation Cloud
/*
 Singleton support
 */
+(Cloud *)sharedInstance{
    if(sharedInstance == nil){
        sharedInstance = [[Cloud alloc] init];
    }
    return sharedInstance;
}

-(id)init{
    if(self = [super init]){
        self.baseURL = @"127.0.0.1:8000";
        self.protocolVersion = @"v1";
        self.isSynchronizing = NO;
        
    }
    return self;
}

-(NSString*)apiURL{
    return [NSString stringWithFormat:@"http://%@/%@", self.baseURL, self.protocolVersion];
}

/**
 Tells the server about our existance
 **/
-(void)registerDevice{
    NSLog(@"Register Device");
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.requestSerializer = [AFJSONRequestSerializer serializer];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    NSString * url = [[self apiURL] stringByAppendingString:@"/register/device/"];
    

    NSString * deviceID = [[Settings sharedInstance] deviceID];
    NSString * deviceType = [[Settings sharedInstance] deviceType];
    
    NSDictionary * parameters = @{@"device_id":deviceID,@"device_type":deviceType};
    
    [manager POST:url parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        NSLog(@"Just successfully registered the device with ID: %@ and type: %@", deviceID, deviceType);
        [[Settings sharedInstance] setRegistered:YES];
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        NSLog(@"Just successfully registered the device with ID: %@ and type: %@", deviceID, deviceType);

        NSLog(@"Error registering: %@ %@", error, [NSString stringWithUTF8String: [operation.responseData bytes]]);
    }];
}

/*
    Asks a question
*/
-(void)askQuestion:(Question *)question delegate:(NSObject<AskQuestionDelegate> *)delegate{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *parameters = @{@"question": @"Will this work?"};
    [manager POST:@"http://google.com" parameters:parameters success:^(AFHTTPRequestOperation *operation, id responseObject) {
        [delegate questionAskDidSucceed:question];
        NSLog(@"JSON: %@", responseObject);
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        //[delegate questionAskDidFail];
        
        [delegate questionAskDidSucceed:question];
        NSLog(@"Error: %@", error);
    }];
}

/*
 Asks the server what they know about us
 */
-(void)getDeviceInfromation:(NSObject<DeviceInformationRequestDelegate> *)delegate{
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    NSDictionary *parameters = @{@"deviceID": @"sjdfj329239fhb32b"};
    [manager GET:@"http://google.com" parameters:parameters success:^(AFHTTPRequestOperation * operation, id responseObject){
        // Success
    }
         failure:^(AFHTTPRequestOperation * operation, NSError * error){
             // Failure
         }
     ];
}

/*
 Goes to the server and asks for all of the information that we have not yet seen
 
 For all thread responses, we get the information and pass it back as an array.
 The delegate can worry about putting the information in the data stores and forcing the UI update.
 */
-(void)synchronize{
    if(!self.isSynchronizing){
        self.isSynchronizing = YES;
        if(![[Settings sharedInstance] didRegister]){ // If we have not registered this device
            [self registerDevice];
        }
        
        /*
         Should return a list of URLS that we should hit for new information
         We need to be careful here
         
         */
        self.isSynchronizing = NO;
    }
}


@end
